#include <Arduino.h>
#include <ArduinoJson.h>
#include <StreamUtils.h>
#include "SerialCommunication.hpp"

const char* _prova = "{state: \"state\", data: 0}";
// {"messageID":2, "data":-1}
// {"messageID":1, "data":40}
// {"messageID":1, "data":50}
// {"messageID":0, "data":-1}

void prova() {
    Serial.println(_prova);
}

void interpretMessage(int valveOpeningLevel, riverControllerMacroStates arduinoState, SensorsController &controller) 
{
    //Serial.println("In interpretMessage()");
    if (Serial.available() > 0) {
        /* String readString = Serial.readString();
        readString.trim(); */
        //int readInt = Serial.parseInt();
        controller.controllerLCDClear();
        controller.controllerLCDPrint("oddio seriale");
        //delay(1000);
        //ReadLoggingStream loggingStream(Serial, Serial); // without this line, the Arduino doesn't receive jsons from java, even if synchronised. Why?
        JsonDocument doc;
        DeserializationError error = deserializeJson(doc, Serial);
        while (isspace(Serial.peek()))
            Serial.read();
        controller.controllerLCDPrint("flushed");
        //delay(1000);
        if (error != DeserializationError::Ok) {
            controller.controllerLCDPrint(":(");
            //delay(3000);
            switch(error.code()) {
                case DeserializationError::Ok:
                    controller.controllerLCDPrint("bro sto trollando");
                    break;
                case DeserializationError::EmptyInput:
                    controller.controllerLCDPrint("empty input");
                    break;
                case DeserializationError::IncompleteInput:
                    controller.controllerLCDPrint("incomplete input");
                    break;
                case DeserializationError::InvalidInput:
                    controller.controllerLCDPrint("invalid input");
                    break;
                case DeserializationError::NoMemory:
                    controller.controllerLCDPrint("no memory");
                    break;
                case DeserializationError::TooDeep:
                    controller.controllerLCDPrint("too deep");
                    break;
                default:
                    controller.controllerLCDPrint("banane");
                    break;
            }
            //delay(10000);
        }
        int messageID = doc["messageID"];
        switch (messageID) {
            case 0: // get opening level
                    controller.controllerLCDPrint("case 0");
                    sendValveMessage(valveOpeningLevel, controller);
                    break;
            case 1: // set opening level
                    controller.controllerLCDPrint("case 1");
                    /* The value received from the backend is a percentage
                       (a number ranging from 0 to 100) and needs to be mapped
                       to a valve opening angle, in the range [0, 180].
                    */
                    controller.controllerSetGate(doc["data"]);
                    sendValveMessage(doc["data"], controller);
                    break;
            case 2: // return the state of the Water Channel Controller
                    controller.controllerLCDPrint("case 2");
                    //prova();
                    sendStateMessage(arduinoState, controller);
                    break;
            default: 
                    controller.controllerLCDPrint("AHSHH");
                    break;
        }
    }
}

void sendStateMessage(int arduinoStateAsInt, SensorsController &controller)
{
    JsonDocument doc;
    doc["messageType"] = "state";
    doc["data"] = arduinoStateAsInt;
    serializeJson(doc, Serial);
    Serial.println(""); // to help the Java program find a '\n'
    //controller.controllerLCDPrint(doc.as<const char *>());
}

void sendValveMessage(int valveOpeningLevel, SensorsController &controller)
{
    JsonDocument doc;
    doc["messageType"] = "valve";
    doc["data"] = valveOpeningLevel;
    serializeJson(doc, Serial);
    Serial.println(""); // to help the Java program find a '\n'
    //controller.controllerLCDPrint(doc.as<const char *>());
}

int mapFromPercentageToAngle(int percentage) {
    return (int) map(percentage, 0, 100, 0, 179);
}

int mapFromAngleToPercentage(int angle) {
    return (int) map(angle, 0, 179, 0, 100);
}