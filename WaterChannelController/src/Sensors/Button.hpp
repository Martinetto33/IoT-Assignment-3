#pragma once

#include <Arduino.h>

class Button {
    private:
        uint8_t buttonPin;
        int buttonState; // current reading of the input pin
        int lastButtonState; // previous reading of the input pin
        unsigned long lastDebounceTime; // the last time the output pin was toggled
        const unsigned long bouncingTime = 30; // expressed in milliseconds
    public:
        Button(uint8_t bPin);
        bool isPressed();
};