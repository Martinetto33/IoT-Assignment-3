#pragma once

#include <Arduino.h>

class Button {
    private:
        uint8_t buttonPin;
    public:
        Button(uint8_t bPin);
        bool isPressed();
};