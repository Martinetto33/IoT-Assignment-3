#pragma once

#include "RiverControllerFSM.hpp"
#include "Task.hpp"

class RiverControllerTask : public Task {
    private:
        RiverControllerFSM fsm;
        void automaticRoutine(int period);
        void manualRoutine(int period);
    public:
        RiverControllerTask();
        void tick(int period) override;
}