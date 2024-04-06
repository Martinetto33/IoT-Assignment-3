package rivermonitoringservice.state.api;

import rivermonitoringservice.data.RiverMonitoringServiceData;

public interface State {
    
    void onEntry();

    void handle(RiverMonitoringServiceData data);

    void onExit();

    String getStateAsString();

    void evaluate(double waterLevel);

}
