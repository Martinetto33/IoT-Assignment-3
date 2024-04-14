#pragma once
#include "ServoMotor.hpp"
#include "LcdScreen.hpp"
#include "Button.hpp"
#include "Potentiometer.hpp"
#include "configPin.hpp"

/*
    The SensorsController is a class that basically represents the
    hardware and exposes high-level methods through which the software
    components (tasks) can interact with the sensors and the actuators.

    There's only one instance of this class, passed to all the objects
    requiring it.
*/

class SensorsController
{
    private:
        ServoMotor servo;
        Potentiometer potentiometer;
        LcdScreen lcd;
        Button btn;
    public:
        SensorsController();
        /* LCD management */
        void controllerLCDPrint(const char* message);
        void controllerLCDClear();

        /* Potentiometer management*/
        int potentiometerValue();
        int mappedPotentiometer();

        /* Servo motor management */
        int controllerOpenGate();
        int controllerCloseGate();
        int controllerSetGate(int angle);
        int getAngle();

        /* Button management */
        bool controllerIsButtonPressed();
};