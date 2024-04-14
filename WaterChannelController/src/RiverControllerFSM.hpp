#pragma once
#include "AbstractFSM.hpp"

/*
    States of this FSM are distinguished in two macro states, "AUTO" and "MANUAL",
    as seen in the first enum. When the system is in "AUTO" mode, the valve is controlled
    via dashboard from a web application. When the system is in "MANUAL" mode, the valve
    is controlled physically through the potentiometer by a real operator.

    Objects of this class can be instantiated with the following code:

    RiverControllerFSM objectName;
*/

enum riverControllerStates {
    AUTO,
    MANUAL
};

class RiverControllerFSM {
    private:
        AbstractFSM<riverControllerStates> fsm;
    public:
        RiverControllerFSM() : fsm(AUTO) {} /* This syntax is weird, but it's needed because there is no
                                                                    no default constructor for the AbstractFSM.
                                                                    
                                                                    More about that at: 
                                                                    https://stackoverflow.com/questions/4981241/no-default-constructor-exists-for-class
                                                                    */
        void setState(riverControllerStates macro_state);

        /* Call this when you want to know if the system is in
        AUTO or MANUAL state. */
        riverControllerStates getState();
};