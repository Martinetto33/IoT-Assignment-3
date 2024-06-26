#pragma once

#include "RiverControllerFSM.hpp"
#include "Task.hpp"
#include "Sensors/SensorsController.hpp"
#include "SerialCommunication.hpp"

class RiverControllerTask : public Task {
    private:
        RiverControllerFSM fsm;
        SensorsController sensorController;
        void automaticRoutine(int period);
        void manualRoutine(int period);
        void checkIfStateMustChange();
    public:
        RiverControllerTask();
        void tick(int period) override;
};