#include <Arduino.h>
#include "RiverControllerTask.hpp"
#include "RiverControllerFSM.hpp"
#include "configConstants.hpp"

void RiverControllerTask::tick(int period)
{
    if (this->fsm.getMacroState() == AUTO) {
        this->automaticRoutine(period);
    } else {
        this->manualRoutine(period);
    }
}

void RiverControllerTask::automaticRoutine(int period)
{
    switch(fsm.getMicroState()) {
        case NORMAL:
        {

        }
        break;
        case ALARM_TOO_LOW:
        {

        }
        break;
        case PRE_ALARM_TOO_HIGH:
        {

        }
        break;
        case ALARM_TOO_HIGH:
        {

        }
        break;
        case ALARM_TOO_HIGH_CRITIC:
        {

        }
        break;
    }
}

void RiverControllerTask::manualRoutine(int period)
{
    
}