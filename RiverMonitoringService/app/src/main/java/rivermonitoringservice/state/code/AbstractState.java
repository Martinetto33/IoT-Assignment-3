package rivermonitoringservice.state.code;

import java.util.Objects;
import rivermonitoringservice.state.api.State;
import rivermonitoringservice.Constants;
import rivermonitoringservice.RiverMonitoringService;
import rivermonitoringservice.channelcontroller.WaterChannelControllerState;
import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;

public abstract class AbstractState implements State {
    private RiverMonitoringServiceFSM fsm;
    private int measurementFrequency;

    public AbstractState(final int measurementFrequency) {
        this.measurementFrequency = measurementFrequency;
    }

    public AbstractState() {
        this(Constants.f1);
    }

    public void attachFSM(final RiverMonitoringServiceFSM fsm) {
        Objects.requireNonNull(fsm);
        this.fsm = fsm;
    }

    public void handle(RiverMonitoringServiceData data) {
        if (data.arduinoState() == WaterChannelControllerState.UNINITIALISED) {
            throw new IllegalStateException("The River Monitoring Service doesn't know if the Water Channel " +
                                             "Controller is in manual or automatic state.");
        }
        /* If a remote user requested to open or close the valve... */
        if (data.openingPercentageRequiredByFrontend().isPresent()) {
            final int requiredPercentage = data.openingPercentageRequiredByFrontend().get();
            if (data.valveOpeningPercentage() != requiredPercentage && data.arduinoState() == WaterChannelControllerState.AUTO) {
                // This operation is slow and cannot happen if the backend does not give the
                // channel controller time to move the valve...
                RiverMonitoringService.serviceWaterChannelController().requestControllerToSetValveOpeningLevel(requiredPercentage);
                RiverMonitoringService.notifyDashboard();
            }
        }
    }

    /* SETTERS */

    protected void setFSMState(final State state) {
        this.fsm.changeState(state);
    }

    protected void setMeasurementFrequency(final int measurementFrequency) {
        this.measurementFrequency = measurementFrequency;
        RiverMonitoringService.updateESPMonitoringSystem(measurementFrequency);
    }

    /* GETTERS */

    public double getMeasurementFrequency() {
        return this.measurementFrequency;
    }

    /* A "gentle" setter */
    protected void suggestValveOpeningLevel(final int openingLevelPercentage) {
        RiverMonitoringService.serviceSharedMemory().setSuggestedValveOpeningLevel(String.valueOf(openingLevelPercentage));
    }

    public abstract String getStateAsString();

    /**
     * {@inheritDoc}
     */
    public State evaluate(final double waterLevel) {
        /* If the current state is appropriate for this water level,
         * do nothing.
         */
        if (this.fsm.getCurrentState().getAssociatedRange().contains(waterLevel)) {
            return this;
        } else {
            if (Constants.lowRange.contains(waterLevel)) {
                return new AlarmTooLowState();
            } else if (Constants.normalRange.contains(waterLevel)) {
                return new NormalState();
            } else if (Constants.preHighRange.contains(waterLevel)) {
                return new PreAlarmTooHighState();
            } else if (Constants.highRange.contains(waterLevel)) {
                return new AlarmTooHighState();
            } else if (Constants.criticalRange.contains(waterLevel)) {
                return new AlarmTooHighCriticState();
            } else {
                System.out.println("Unearthly water level received: " + waterLevel);
                return this;
            }
        }
    }
}
