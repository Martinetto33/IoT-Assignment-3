package rivermonitoringservice.fsm;

import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.state.api.State;

public class RiverMonitoringServiceFSM {
    private State currentState;

    public RiverMonitoringServiceFSM() {}

    public State getCurrentState() {
        return this.currentState;
    }

    public void changeState(final State newState) {
        if (this.currentState != null) {
            this.currentState.onExit();
        }
        this.currentState = newState;
        this.currentState.attachFSM(this);
        this.currentState.onEntry();
    }

    public void handle(RiverMonitoringServiceData data) {
        this.currentState.handle(data);
        this.changeState(this.currentState.evaluate(data.waterLevel()));
    }
}
