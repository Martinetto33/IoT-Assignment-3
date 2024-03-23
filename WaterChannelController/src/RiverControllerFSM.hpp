#pragma once
#include "AbstractFSM.hpp"

/*
    States of this FSM are distinguished in two macro states, "AUTO" and "MANUAL",
    as seen in the first enum. The "AUTO" macro state is then logically divided
    into more fine-grained states, called micro states, that model the specifics
    of the project.

    A RiverControllerFSM substantially contains two AbstractFSMs, one
    for the macro states and one for the micro states.

    Objects of this class can be instantiated with the following code:

    RiverControllerFSM objectName;
*/

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
        RiverControllerFSM() : macroFSM(MANUAL), microFSM(NORMAL) {} /* This syntax is weird, but it's needed because there is no
                                                                    no default constructor for the AbstractFSM.
                                                                    
                                                                    More about that at: 
                                                                    https://stackoverflow.com/questions/4981241/no-default-constructor-exists-for-class
                                                                    */
        void setMacroState(riverControllerMacroStates macro_state);
        void setMicroState(riverControllerMicroStates micro_state);

        /* Call this when you want to know if the system is in
        AUTO or MANUAL state. */
        riverControllerMacroStates getMacroState();

        /* Call this when you want to know in which specific micro state
        of the "AUTO" macro state the FSM is.*/
        riverControllerMicroStates getMicroState();
};