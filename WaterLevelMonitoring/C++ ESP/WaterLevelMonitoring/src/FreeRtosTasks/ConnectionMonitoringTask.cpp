#include "ConnectionMonitoringTask.hpp"
#include "WiFi-Module/Mqtt-Utilities.h"

ConnectionMonitoringTask::ConnectionMonitoringTask() {
    this->canExecute = true;
}

/* When calling this function, the RTOS task should stop the execution of all
the other tasks, by changing the state of the macro-system. */
bool ConnectionMonitoringTask::isConnectionOk() {
    return mqtt_is_client_connected();
}

void ConnectionMonitoringTask::reconnect() {
    mqtt_reconnect();
}

void ConnectionMonitoringTask::pause() {
    this->canExecute = false;
}

void ConnectionMonitoringTask::resume() {
    this->canExecute = true;
}