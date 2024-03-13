#pragma once
#include "AbstractTask.hpp"

class FrequencyReceiverTask : AbstractTask {
    private:
        int measurementFrequency;
    public:
        FrequencyReceiverTask(int mFrequency);
        void setMeasurementFrequency(int mFrequency);
        int getMeasurementFrequency();
        void pause() override;
        void resume() override;
};