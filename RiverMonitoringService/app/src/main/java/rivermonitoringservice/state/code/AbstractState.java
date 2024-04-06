package rivermonitoringservice.state.code;

import java.util.Objects;
import java.util.Optional;
import rivermonitoringservice.state.api.State;
import rivermonitoringservice.Constants;
import rivermonitoringservice.MessageID;
import rivermonitoringservice.RiverMonitoringService;
import rivermonitoringservice.WaterChannelControllerState;
import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;

public abstract class AbstractState implements State {
    private final RiverMonitoringServiceFSM fsm;
    private double measurementFrequency;
    private double currentWaterLevel;

    public AbstractState(final double measurementFrequency, final double currentWaterLevel, final RiverMonitoringServiceFSM fsm) {
        this.measurementFrequency = measurementFrequency;
        this.currentWaterLevel = currentWaterLevel;
        Objects.requireNonNull(fsm);
        this.fsm = fsm;
    }

    public AbstractState(final RiverMonitoringServiceFSM fsm) {
        this(Constants.f1, Constants.waterLevel1, fsm);
    }

    public void handle(RiverMonitoringServiceData data) {
        if (data.arduinoState() == WaterChannelControllerState.UNINITIALISED) {
            throw new IllegalStateException("The River Monitoring Service doesn't know if the Water Channel " +
                                             "Controller is in manual or automatic state.");
        }
        /* Most states require to take measurements and handle occasional inputs from frontend. */
        this.currentWaterLevel = data.waterLevel();
        if (data.openingPercentageRequiredByFrontend().isPresent()) {
            final int requiredPercentage = data.openingPercentageRequiredByFrontend().get();
            if (data.valveOpeningPercentage() != requiredPercentage && data.arduinoState() == WaterChannelControllerState.AUTO) {
                RiverMonitoringService.updateChannelController(MessageID.SET_OPENING_LEVEL, Optional.of(requiredPercentage));
            }
        }
        RiverMonitoringService.updateDashboard(this.currentWaterLevel, data.valveOpeningPercentage(), this.getStateAsString());
    }

    /* SETTERS */

    protected void setFSMState(final State state) {
        this.fsm.changeState(state);
    }

    protected void setMeasurementFrequency(final double measurementFrequency) {
        this.measurementFrequency = measurementFrequency;
    }

    /* GETTERS */

    public double getMeasurementFrequency() {
        return this.measurementFrequency;
    }

    public double getCurrentWaterLevel() {
        return this.currentWaterLevel;
    }

    public abstract String getStateAsString();

    /**
     * This method should be called from outside this class, right after calling the handle() method.
     * This is because the state might change after a call to this method.
     * Currently, this method is called in the handle() method of the system finite state machine 
     * {@link rivermonitoringservice.fsm.RiverMonitoringServiceFSM}.
     * @param waterLevel the current water level.
     */
    public abstract void evaluate(final double waterLevel); // to set the FSM state according to the water level
}
