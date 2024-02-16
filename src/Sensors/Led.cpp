#include "Led.hpp"
#include <Arduino.h>

Led::Led(uint8_t pin)
{
    this->ledPin = pin;
    pinMode(pin, OUTPUT);
}

void Led::turnOn()
{
    digitalWrite(this->ledPin, HIGH);
}

void Led::turnOff()
{
    digitalWrite(this->ledPin, LOW);
}
