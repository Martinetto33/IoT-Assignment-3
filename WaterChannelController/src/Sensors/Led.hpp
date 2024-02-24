#pragma once

#include <Arduino.h>

class Led {
    private:
        uint8_t ledPin;
    public:
        Led(uint8_t pin);
        void turnOn();
        void turnOff();
};