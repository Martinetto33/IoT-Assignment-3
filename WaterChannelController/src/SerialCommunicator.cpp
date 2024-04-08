#include <Arduino.h>
#include <ArduinoJson.h>
#include "SerialCommunication.hpp"

void interpretMessage(int valveOpeningLevel, riverControllerMacroStates arduinoState, SensorsController controller) 
{
    //Serial.println("In interpretMessage()");
    JsonDocument doc;
    deserializeJson(doc, Serial);
    int messageID = doc["messageID"];
    switch (messageID) {
        case 0: // get opening level
                controller.controllerLCDPrint("case 0");
                Serial.println(valveOpeningLevel);
                break;
        case 1: // set opening level
                controller.controllerLCDPrint("case 1");
                controller.controllerSetGate(doc["data"]);
                break;
        case 2: // return the state of the Water Channel Controller
                controller.controllerLCDPrint("case 3");
                Serial.println(arduinoState);
                break;
        default: 
        controller.controllerLCDPrint("AHSHH");
        break;
    }
     
}