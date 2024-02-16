#include "Button.hpp"

Button::Button(uint8_t bPin)
{
    this->buttonPin = bPin;
}

bool Button::isPressed()
{
    return this->buttonPin == HIGH;
}
