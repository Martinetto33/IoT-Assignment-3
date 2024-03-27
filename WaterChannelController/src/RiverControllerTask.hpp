#pragma once

#include "RiverControllerFSM.hpp"
#include "Task.hpp"
#include "SensorsController.hpp"

class RiverControllerTask : public Task {
    private:
        RiverControllerFSM fsm;
        SensorsController sensorController;
        void automaticRoutine(int period);
        void manualRoutine(int period);
    public:
        RiverControllerTask();
        void tick(int period) override;
};