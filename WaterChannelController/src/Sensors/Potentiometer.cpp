#include "Potentiometer.hpp"
#include <Arduino.h>

Potentiometer::Potentiometer(uint8_t pin)
{
    this->potentiometerPin = pin;
    pinMode(pin, INPUT);
}

int Potentiometer::update()
{
    return analogRead(this->potentiometerPin);
}