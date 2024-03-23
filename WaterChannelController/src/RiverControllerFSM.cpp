#include "RiverControllerFSM.hpp"
#include "AbstractFSM.hpp"

void RiverControllerFSM::setMacroState(riverControllerMacroStates macro_state) {
    macroFSM.setCurrentState(macro_state);
}

/* A microstate setter, that affects the more specific FSM. */
void RiverControllerFSM::setMicroState(riverControllerMicroStates micro_state) {
    microFSM.setCurrentState(micro_state);
}

/* A macrostate getter. */
riverControllerMacroStates RiverControllerFSM::getMacroState() {
    return macroFSM.getCurrentState();
}

/* A microstate getter. */
riverControllerMicroStates RiverControllerFSM::getMicroState() {
    return microFSM.getCurrentState();
}