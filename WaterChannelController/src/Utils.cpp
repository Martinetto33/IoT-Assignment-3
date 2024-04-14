#include <Arduino.h>
#include "Utils.hpp"

int mapFromPercentageToAngle(int percentage) {
    return (int) map(percentage, 0, 100, 0, 179);
}

int mapFromAngleToPercentage(int angle) {
    return (int) map(angle, 0, 179, 0, 100);
}