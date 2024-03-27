#include <Arduino.h>
#include "RiverControllerTask.hpp"
#include "RiverControllerFSM.hpp"
#include "configConstants.hpp"

RiverControllerTask::RiverControllerTask()
{
}

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
            //frequenza usata per monitorare l'acqua è F1
            //la valvola deve essere aperta al 25%
            this->sensorController.controllerSetGate(45);
        }
        break;
        case ALARM_TOO_LOW:
        {
            //frequenza sempre F1 ??
            //la valvola deve essere aperta allo 0% (chiusa)
            this->sensorController.controllerCloseGate();
        }
        break;
        case PRE_ALARM_TOO_HIGH:
        {
            //frequenza usata per monitorare l'acqua è F2 (F2 > F1)
            //l'apertura della valvola rimane com'era
        }
        break;
        case ALARM_TOO_HIGH:
        {
            //frequenza usata per monitorare l'acqua è sempre F2
            //la valvola deve essere aperta al 50%
            this->sensorController.controllerSetGate(90);
        }
        break;
        case ALARM_TOO_HIGH_CRITIC:
        {
            //frequenza usata per monitorare l'acqua è sempre F2
            //la valvola deve essere aperta al 100%
            this->sensorController.controllerOpenGate();
        }
        break;
    }
}

void RiverControllerTask::manualRoutine(int period)
{
    //gestire l'apertura della valvola in base al potenziometro
    this->sensorController.controllerSetGate(this->sensorController.mappedPotentiometer());
}