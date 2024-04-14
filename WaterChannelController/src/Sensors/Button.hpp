#pragma once

#include <Arduino.h>

#define BUTTON_BOUNCING_TIME_MS 30

class Button {
    private:
        uint8_t buttonPin;
        int buttonState;                // needed for debouncing
        int lastButtonState;            // needed for debouncing
        unsigned long lastDebounceTime; // needed for debouncing
    public:
        Button(uint8_t bPin);
        bool isPressed();
};