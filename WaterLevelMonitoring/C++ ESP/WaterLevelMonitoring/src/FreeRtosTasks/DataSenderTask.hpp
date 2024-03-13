#pragma once
#include "AbstractTask.hpp"
#include "WiFi-Module/Mqtt-Utilities.h"

class DataSenderTask : AbstractTask {
    public:
        DataSenderTask();
        void sendData(float data);
        void pause() override;
        void resume() override;
};