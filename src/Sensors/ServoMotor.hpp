#pragma once
#include <Servo.h>

class ServoMotor{
    private:
        int pin; 
        int angle;
        Servo motor; 

    public:
        ServoMotor(int pin);
        void on();
        void setPosition(int angle);
        int getAngle();
        void off();
};