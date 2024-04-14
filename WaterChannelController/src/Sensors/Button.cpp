#include "Button.hpp"

Button::Button(uint8_t bPin)
{
    this->buttonPin = bPin;
    this->lastButtonState = LOW;
}

/**
 * Code taken from: https://docs.arduino.cc/built-in-examples/digital/Debounce/
*/
bool Button::isPressed()
{
    int reading = digitalRead(this->buttonPin);
    /* If the new reading is different from the last button
    state, reset the timer. */
    if (reading != this->lastButtonState) {
        this->lastDebounceTime = millis();
    }
    if (millis() - this->lastDebounceTime > BUTTON_BOUNCING_TIME_MS) {
        if (reading != this->buttonState) {
            this->buttonState = reading;
        }
    }
    return this->buttonState == HIGH;
}
