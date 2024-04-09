#include <Arduino.h>
#include <ArduinoJson.h>
#include "SerialCommunication.hpp"

const char* _prova = "{state: \"state\", data: 0}";

void prova() {
    Serial.println(_prova);
}

void interpretMessage(int valveOpeningLevel, riverControllerMacroStates arduinoState, SensorsController controller) 
{
    //Serial.println("In interpretMessage()");
    if (Serial.available() > 0) {
        /* String readString = Serial.readString();
        readString.trim(); */
        controller.controllerLCDClear();
        controller.controllerLCDPrint("oddio seriale");
        JsonDocument doc;
        deserializeJson(doc, Serial);
        int messageID = doc["messageID"];
        switch (messageID) {
            case 0: // get opening level
                    controller.controllerLCDPrint("case 0");
                    sendValveMessage(valveOpeningLevel, controller);
                    break;
            case 1: // set opening level
                    controller.controllerLCDPrint("case 1");
                    controller.controllerSetGate(doc["data"]);
                    break;
            case 2: // return the state of the Water Channel Controller
                    controller.controllerLCDPrint("case 3");
                    prova();
                    //sendStateMessage(arduinoState, controller);
                    break;
            default: 
                    controller.controllerLCDPrint("AHSHH");
                    break;
        }
    }
}


void sendStateMessage(int arduinoStateAsInt, SensorsController controller)
{
    JsonDocument doc;
    doc["messageType"] = "state";
    doc["data"] = arduinoStateAsInt;
    serializeJson(doc, Serial);    
    controller.controllerLCDPrint(doc.as<const char *>());
}

void sendValveMessage(int valveOpeningLevel, SensorsController controller)
{
    JsonDocument doc;
    doc["messageType"] = "valve";
    doc["data"] = valveOpeningLevel;
    serializeJson(doc, Serial);
    controller.controllerLCDPrint(doc.as<const char *>());
}
