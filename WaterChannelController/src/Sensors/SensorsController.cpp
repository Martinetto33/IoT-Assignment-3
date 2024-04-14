#include <Arduino.h>
#include "SensorsController.hpp"

/* The constants used in the default constructors are defined in configPin.hpp */
SensorsController::SensorsController() : servo(SERVO_PIN), potentiometer(POTENTIOMETER_PIN),
                                     lcd(LCD_SDA_PIN, LCD_SCL_PIN), btn(BUTTON_PIN){}

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

int SensorsController::getAngle() {
    return this->servo.getAngle();
}

bool SensorsController::controllerIsButtonPressed()
{
    return this->btn.isPressed();
}
