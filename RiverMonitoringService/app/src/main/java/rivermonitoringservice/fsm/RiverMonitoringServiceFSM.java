package rivermonitoringservice.fsm;

import rivermonitoringservice.SharedMemory.SharedMemory;
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

    /**
     * This FSM manages the system based on the current state. It has access to the shared memory
     * to update the system state as well.
     * @param data the data polled by the monitoring service.
     * @param shMemory the shared memory containing the current data derived from the sensors.
     */
    public void handle(RiverMonitoringServiceData data, final SharedMemory shMemory) {
        this.currentState.handle(data);
        final State nextState = this.currentState.evaluate(data.waterLevel());
        if (nextState != this.currentState) {
            this.changeState(nextState);
            /* Update the shared memory with the state of the system. */
            shMemory.setStatus(nextState.getStateAsString());
        }
    }
}
