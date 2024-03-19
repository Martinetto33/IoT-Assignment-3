#pragma once
#include "AbstractFSM.hpp"

enum riverControllerMacroStates {
    AUTO,
    MANUAL
};

enum riverControllerMicroStates {
    NORMAL, /* the valve opening level should be 25% */
    ALARM_TOO_LOW, /* the valve opening level should be 0% */
    PRE_ALARM_TOO_HIGH, /* no changes */
    ALARM_TOO_HIGH, /* the valve opening level should be 50% */
    ALARM_TOO_HIGH_CRITIC /* the valve opening level should be 100% */
};

class RiverControllerFSM {
    private:
        AbstractFSM<riverControllerMacroStates> macroFSM;
        AbstractFSM<riverControllerMicroStates> microFSM;
    public:
        RiverControllerFSM() : macroFSM(MANUAL), microFSM(NORMAL) {}
        void setMacroState(riverControllerMacroStates macro_state);
        void setMicroState(riverControllerMicroStates micro_state);

        riverControllerMacroStates getMacroState();

        riverControllerMicroStates getMicroState();
};