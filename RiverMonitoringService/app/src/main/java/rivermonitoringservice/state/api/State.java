package rivermonitoringservice.state.api;

import com.google.common.collect.Range;

import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;

public interface State {
    
    void onEntry();

    void handle(RiverMonitoringServiceData data);

    void onExit();

    String getStateAsString();

    /**
     * This method should be called from outside this class, right after calling the handle() method.
     * This is because the state might change after a call to this method.
     * Currently, this method is called in the handle() method of the system finite state machine 
     * {@link rivermonitoringservice.fsm.RiverMonitoringServiceFSM}.
     * @param waterLevel the current water level.
     */
    State evaluate(double waterLevel);

    Range<Double> getAssociatedRange();

    void attachFSM(RiverMonitoringServiceFSM fsm);
}
