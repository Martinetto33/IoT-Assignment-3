#pragma once
#include "AbstractTask.hpp"
#include "WiFi-Module/Mqtt-Utilities.h"
#include "WiFi-Module/WiFi-Utilities.h"

class ConnectionMonitoringTask : AbstractTask {
    public:
        ConnectionMonitoringTask();
        bool isConnectionOk();
        void reconnect();
        void pause() override;
        void resume() override;
};
