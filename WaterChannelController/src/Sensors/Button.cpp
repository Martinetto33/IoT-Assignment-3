#include "Button.hpp"

Button::Button(uint8_t bPin)
{
    this->buttonPin = bPin;
    this->buttonState = LOW;
    this->lastButtonState = LOW;
}


/**
 * The following code was adapted from the following source:
 * https://docs.arduino.cc/built-in-examples/digital/Debounce/
*/
bool Button::isPressed()
{
    int reading = digitalRead(this->buttonPin);

    /* If the state has changed due to pressing or bouncing */
    if (reading != this->lastButtonState) {
        // reset the debouncing timer
        this->lastDebounceTime = millis();
    }

    if ((millis() - this->lastDebounceTime) > this->bouncingTime) {
        /* The button reading is now reliable and not due to bouncing. */
        if (reading != this->buttonState) {
            this->buttonState = reading;
            if (this->buttonState == HIGH) {
                this->lastButtonState = HIGH;
                return true;
            }
        }
    }
    lastButtonState = reading;
    return false;
}
