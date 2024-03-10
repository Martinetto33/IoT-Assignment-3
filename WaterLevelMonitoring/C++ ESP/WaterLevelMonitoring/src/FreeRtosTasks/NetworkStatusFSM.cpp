#include "NetworkStatusFSM.hpp"

/**
 * Constructor for a NetworkStatusFSM.
*/
NetworkStatusFSM::NetworkStatusFSM() : fsm(NETWORK_OK) {}

/**
 * Getter of the current state.
*/
task_state NetworkStatusFSM::getState() {
    return this->fsm.getCurrentState();
}

/**
 * Setter of the current state.
*/
void NetworkStatusFSM::setState(task_state newState) {
    this->fsm.setCurrentState(newState);
}