#pragma once
#include "AbstractTask.hpp"

class ConnectionMonitoringTask : AbstractTask {
    public:
        ConnectionMonitoringTask();
        bool isConnectionOk();
        void reconnect();
        void pause() override;
        void resume() override;
};
