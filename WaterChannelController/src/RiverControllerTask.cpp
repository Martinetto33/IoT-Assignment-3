#include <Arduino.h>
#include "RiverControllerTask.hpp"
#include "RiverControllerFSM.hpp"
#include "configConstants.hpp"
#include "Utils.hpp"

extern volatile bool interruptOccurred; // defined in main.cpp

RiverControllerTask::RiverControllerTask()
{
}

void RiverControllerTask::checkIfStateMustChange()
{
    if (this->sensorController.controllerIsButtonPressed()) {
        if (this->fsm.getState() == AUTO) {
            this->fsm.setState(MANUAL);
        } else {
            this->fsm.setState(AUTO);
        }
    }
}

void RiverControllerTask::tick(int period)
{
    this->checkIfStateMustChange();
    interpretMessage(this->sensorController.getAngle(), this->fsm.getState(), this->sensorController); // defined in SerialCommunicator.cpp
    if (this->fsm.getState() == AUTO) {
        this->automaticRoutine(period);
    } else {
        this->manualRoutine(period);
    }
}

void RiverControllerTask::automaticRoutine(int period)
{
    /**
     * The system is in idle. The current design specifies that
     * the Water Channel Controller, if in automatic mode, only waits
     * for instructions coming fromt the backend.
    */
}

void RiverControllerTask::manualRoutine(int period)
{
    // the valve is opened to the level set by the potentiometer
    this->sensorController.controllerSetGate(this->sensorController.mappedPotentiometer());
    char buffer[sizeof(int)];
    sprintf(buffer, "%d", mapFromAngleToPercentage(this->sensorController.getAngle()));
    this->sensorController.controllerLCDPrint(buffer);
}
