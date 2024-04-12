package rivermonitoringservice.state.api;

import com.google.common.collect.Range;

import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;

public interface State {
    
    void onEntry();

    void handle(RiverMonitoringServiceData data);

    void onExit();

    String getStateAsString();

    State evaluate(double waterLevel);

    Range<Double> getAssociatedRange();

    void attachFSM(RiverMonitoringServiceFSM fsm);
}
