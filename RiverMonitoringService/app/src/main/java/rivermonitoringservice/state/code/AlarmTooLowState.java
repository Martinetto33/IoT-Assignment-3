package rivermonitoringservice.state.code;

import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;

public class AlarmTooLowState extends AbstractState {

    public AlarmTooLowState(RiverMonitoringServiceFSM fsm) {
        super(fsm);
    }

    @Override
    public void onEntry() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onEntry'");
    }

    @Override
    public void onExit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onExit'");
    }

    @Override
    public String getStateAsString() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStateAsString'");
    }

    @Override
    public void evaluate(double waterLevel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }
    
}
