#include "SonarSensor.hpp"
#include <Arduino.h>

SonarSensor::SonarSensor(int currentPinIn, int currentPinOut) {
    pinIn = currentPinIn;
    pinOut = currentPinOut;
    pinMode(currentPinIn, INPUT);
    pinMode(currentPinOut, OUTPUT);
    distance = update();
}

float SonarSensor::update() {
    /* invio impulso */
    digitalWrite(pinOut,LOW);
    delayMicroseconds(3);
    digitalWrite(pinOut,HIGH);
    delayMicroseconds(5);
    digitalWrite(pinOut,LOW);
    
    /* ricevi l’eco */
    float tUS = pulseIn(pinIn, HIGH);
    float t = tUS / 1000.0 / 1000.0 / 2;
    float distance = t*vs;
    return distance;
}