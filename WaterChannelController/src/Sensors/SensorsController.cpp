#include <Arduino.h>
#include "SensorsController.hpp"

#define TemperatureAlarm 35
#define CarIn 1
#define CarOut 5

SensorsController::SensorsController() : sonar(SONAR_PIN_ECHO, SONAR_PIN_TRIGGER), servo(SERVO_PIN), potentiometer(POTENTIOMETER_PIN),
                                     lcd(LCD_SDA_PIN, LCD_SCL_PIN), L1(L1_PIN), L2(L2_PIN), btn(BUTTON_PIN){}

/* LCD management */
void SensorsController::controllerLCDPrint(const char* message)
{
    this->lcd.lcdPrint(message);
}

void SensorsController::controllerLCDClear()
{
    this->lcd.lcdClear();
}

/*Potentiometer management*/

int SensorsController::potentiometerValue()
{
    return this->potentiometer.update();
}

int SensorsController::mappedPotentiometer()
{
    return this->potentiometer.mappedValue();
}

/* Servo motor management */
int SensorsController::controllerOpenGate()
{
    this->servo.on();
    this->servo.setPosition(180);
    this->servo.off();
    return this->servo.getAngle();
}

int SensorsController::controllerCloseGate()
{
    this->servo.on();
    this->servo.setPosition(0);
    this->servo.off();
    return this->servo.getAngle();
}

int SensorsController::controllerSetGate(int angle)
{
    this->servo.on();
    this->servo.setPosition(angle);
    this->servo.off();
    return this->servo.getAngle();
}

/* Sonar management */
float SensorsController::controllerMeasureDistance()
{
    return this->sonar.update();
}

/* LED management */
void SensorsController::controllerTurnOnLED(int ledNumber)
{
    Led* requiredLed = this->determineLEDFromIndex(ledNumber);
    if (requiredLed != nullptr) {
        requiredLed->turnOn();
    }
}

void SensorsController::controllerTurnOffLED(int ledNumber)
{
    Led* requiredLed = this->determineLEDFromIndex(ledNumber);
    if (requiredLed != nullptr) {
        requiredLed->turnOff();
    }
}

bool SensorsController::controllerIsButtonPressed()
{
    return digitalRead(BUTTON_PIN)==HIGH;
}

Led* SensorsController::determineLEDFromIndex(int index)
{
    switch(index) {
        case 1:
            return &(this->L1);
            break;
        case 2:
            return &(this->L2);
            break;
        default:
            return nullptr;
            break;
    }
}
