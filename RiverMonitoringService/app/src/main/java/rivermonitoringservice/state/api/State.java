package rivermonitoringservice.state.api;

import com.google.common.collect.Range;

import rivermonitoringservice.data.RiverMonitoringServiceData;

public interface State {
    
    void onEntry();

    void handle(RiverMonitoringServiceData data);

    void onExit();

    String getStateAsString();

    void evaluate(double waterLevel);

    Range<Double> getAssociatedRange();
}
