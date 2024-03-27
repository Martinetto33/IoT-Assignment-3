#pragma once
#include <Arduino.h>

class Potentiometer
{
    private:
        uint8_t potentiometerPin;
    public:
        Potentiometer(uint8_t potentiometerPin);
        int update();
        int mappedValue();
};