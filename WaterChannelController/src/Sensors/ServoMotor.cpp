#include "ServoMotor.hpp"
#include "Arduino.h"

ServoMotor::ServoMotor(int pin){
    this->pin = pin; 
    this->angle = 0;
    pinMode(pin, OUTPUT);
    this->angle = 179;
} 

void ServoMotor::on(){
    motor.attach(pin, 500, 2915);    
}

void ServoMotor::setPosition(int ang) {
    on();
    if (this->angle > ang)
    {
        for (int k = this->angle; k >= ang; k--)
        {
            delay(5);
            motor.write(k);
        }
    } else {
        for (int k = this->angle; k <= ang; k++)
        {
            motor.write(k);
            delay(5);
        }
    }
    this->angle = ang;
}

// void ServoMotor::setPosition(int ang){
//     on();
//     if(ang<0)
//     {  
//         for (int k = this->angle; k > (this->angle + ang); k--)
//         {
//             delay(5);
//             motor.write(k);
//         }
//     } else {
//         for (int k = this->angle; k < (this->angle + ang); k++)
//         {
//             motor.write(k);
//             delay(5);
//         }
//     }
//     this->angle = this->angle + ang;
// }

int ServoMotor::getAngle() 
{
    return this->angle;
}
void ServoMotor::off(){
    motor.detach();    
}