#pragma once
#include <FreeRTOS.h>
#include <Arduino.h>

class AbstractTask {
    /* In C++, derived classes can only access the fields of the base class
    if they are declared as protected or public:
    https://stackoverflow.com/questions/15117591/why-is-inherited-member-not-allowed
    */
    protected:
        bool canExecute; /* set to false when the task needs to be paused, e.g. when the system is in NETWORK_ERROR status */
    public:
        virtual void pause() = 0;
        virtual void resume() = 0;
};