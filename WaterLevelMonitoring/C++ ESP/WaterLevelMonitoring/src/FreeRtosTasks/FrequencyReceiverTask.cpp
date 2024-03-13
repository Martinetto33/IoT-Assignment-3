#include "FrequencyReceiverTask.hpp"

FrequencyReceiverTask::FrequencyReceiverTask(int mFrequency) {
    this->measurementFrequency = mFrequency;
    this->canExecute = true;
}

void FrequencyReceiverTask::setMeasurementFrequency(int mFrequency) {
    this->measurementFrequency = mFrequency;
}

int FrequencyReceiverTask::getMeasurementFrequency() {
    return this->measurementFrequency;
}

void FrequencyReceiverTask::pause() {
    this->canExecute = false;
}

void FrequencyReceiverTask::resume() {
    this->canExecute = true;
}