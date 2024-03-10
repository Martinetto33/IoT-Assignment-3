#include "AbstractFiniteStateMachine.hpp"

/**
 * A base FSM for the Task manager.
*/

enum task_state {NETWORK_OK, NETWORK_ERROR};

class NetworkStatusFSM {
    private:
        AbstractFiniteStateMachine<task_state> fsm;
    public:
        NetworkStatusFSM();
        task_state getState();
        void setState(task_state newState);
};