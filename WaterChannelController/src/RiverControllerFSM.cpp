#include "RiverControllerFSM.hpp"
#include "AbstractFSM.hpp"

void RiverControllerFSM::setState(riverControllerStates state) {
    fsm.setCurrentState(state);
}

/* A macrostate getter. */
riverControllerStates RiverControllerFSM::getState() {
    return fsm.getCurrentState();
}