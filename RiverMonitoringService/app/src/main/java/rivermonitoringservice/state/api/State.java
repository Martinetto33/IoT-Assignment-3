package rivermonitoringservice.state.api;

public interface State {
    
    void onEntry();

    void handle();

    void onExit();

}
