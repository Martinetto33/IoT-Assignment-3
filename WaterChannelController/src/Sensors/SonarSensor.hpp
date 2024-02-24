#pragma once

class SonarSensor
{
    private:
        int pinIn;
        int pinOut;
        float distance;
        const float vs = 331.45 + 0.62*20;
        
    public:
        SonarSensor(int currentPinIn, int currentPinOut);
        float update();
};