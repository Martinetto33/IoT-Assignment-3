package rivermonitoringservice.state.code;

import rivermonitoringservice.state.api.State;

public class NormalState implements State {

    @Override
    public void onEntry() {
        //serial to arduino
    }

    @Override
    public void handle() {
         
        //get mqtt
        //set dashboard
    }

    @Override
    public void onExit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onExit'");
    }
    
}
