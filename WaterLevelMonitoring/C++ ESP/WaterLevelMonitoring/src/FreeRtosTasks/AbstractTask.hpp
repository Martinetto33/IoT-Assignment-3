#pragma once
#include <FreeRTOS.h>

class AbstractTask {
    private:
        bool canExecute; /* set to false when the task needs to be paused, e.g. when the system is in NETWORK_ERROR status */
        bool isPaused; /* set to true when the task is paused, in case it might be useful for the resume() method */
    public:
        AbstractTask();
        void pause();
        void resume();
};