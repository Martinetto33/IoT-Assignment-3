#pragma once
#include <FreeRTOS.h>
#include "AbstractFiniteStateMachine.hpp"

enum task_state {NETWORK_OK, NETWORK_ERROR};

class AbstractTaskFSM {
    private:
        AbstractFiniteStateMachine<task_state> fsm;
    public:
        AbstractTaskFSM();
        task_state getState();
        void setState(task_state newState);
};

class AbstractTask {
    private:
        AbstractTaskFSM fsm;
        bool canExecute;
        bool isPaused;
    public:
        AbstractTask(AbstractTaskFSM fsm);
        void run();
        void pause();
        void resume();
};