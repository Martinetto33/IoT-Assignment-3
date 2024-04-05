package rivermonitoringservice.state.code;

import rivermonitoringservice.state.api.State;

import java.util.Objects;

import rivermonitoringservice.Constants;
import rivermonitoringservice.RiverMonitoringService;
import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;

public abstract class AbstractState implements State {
    private final RiverMonitoringServiceFSM fsm;
    private double measurementFrequency;
    private double currentWaterLevel;
    private WaterChannelControllerState arduinoState = WaterChannelControllerState.UNINITIALISED;

    protected enum WaterChannelControllerState {
        UNINITIALISED,
        MANUAL,
        AUTO
    }

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
        if (this.arduinoState == WaterChannelControllerState.UNINITIALISED) {
            throw new IllegalStateException("The River Monitoring Service doesn't know if the Water Channel" +
                                             "Controller is in manual or automatic state.");
        }
        /* Most states require to take measurements and handle occasional inputs from frontend. */
        this.currentWaterLevel = data.waterLevel();
        if (data.frontendRequiredOpeningPercentage().isPresent()) {
            final int requiredPercentage = data.frontendRequiredOpeningPercentage().get();
            if (data.valveOpeningPercentage() != requiredPercentage && this.arduinoState == WaterChannelControllerState.AUTO) {
                // TODO: set arduino valve to new percentage
            }
        }
        RiverMonitoringService.updateDashboard(this.currentWaterLevel, data.valveOpeningPercentage(), this.getStateAsString());
    }

    /* SETTERS */

    protected void setFSMState(final State state) {
        this.fsm.changeState(state);
    }

    protected void setWaterChannelControllerState(final WaterChannelControllerState state) {
        this.arduinoState = state;
    }

    protected void setMeasurementFrequency(final double measurementFrequency) {
        this.measurementFrequency = measurementFrequency;
    }

    public void setCurrentWaterLevel(final double waterLevel) {
        this.currentWaterLevel = waterLevel;
    }

    /* GETTERS */

    protected WaterChannelControllerState getWaterChannelControllerState() {
        return this.arduinoState;
    }

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
     * @param waterLevel the current water level.
     */
    public abstract void evaluate(final double waterLevel); // to set the FSM state according to the water level
}
