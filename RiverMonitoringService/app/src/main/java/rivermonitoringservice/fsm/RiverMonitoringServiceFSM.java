package rivermonitoringservice.fsm;

import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.state.api.State;

public class RiverMonitoringServiceFSM {
    private State currentState;

    public RiverMonitoringServiceFSM(final State initialState) {
        this.currentState = initialState;
        this.currentState.onEntry();
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void changeState(final State newState) {
        this.currentState.onExit();
        this.currentState = newState;
        this.currentState.onEntry();
    }

    public void handle(RiverMonitoringServiceData data) {
        this.currentState.handle(data);
    }
}
