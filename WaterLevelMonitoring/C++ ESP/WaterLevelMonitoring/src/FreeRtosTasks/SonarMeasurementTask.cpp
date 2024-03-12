#include "SonarMeasurementTask.hpp"

extern const int SONAR_PIN_ECHO; // defined in config.h
extern const int SONAR_PIN_TRIG; // defined in config.h

// TODO: implement all inherited methods from AbstractTask;
// remember to initialise all fields of this Task.
SonarMeasurementTask::SonarMeasurementTask() : sonar(SONAR_PIN_ECHO, SONAR_PIN_TRIG) {
    this->canExecute = true;
}

void SonarMeasurementTask::measureWaterLevel() {
    this->measurementsQueue.add(this->sonar.update());
}

float SonarMeasurementTask::getLastMeasurement() {
    return this->measurementsQueue.poll();
}

bool SonarMeasurementTask::isMeasurementAvailable() {
    return !this->measurementsQueue.isEmpty();
}

void SonarMeasurementTask::setMeasurementFrequency(int newMeasurementFrequency) {
    this->measurementFrequency = newMeasurementFrequency;
}

int SonarMeasurementTask::getMeasurementFrequency() {
    return this->measurementFrequency;
}

void SonarMeasurementTask::pause() {
    this->canExecute = false;
}

void SonarMeasurementTask::resume() {
    this->canExecute = true;
}
