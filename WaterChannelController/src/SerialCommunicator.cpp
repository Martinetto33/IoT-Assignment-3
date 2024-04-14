#include <Arduino.h>
#include <ArduinoJson.h>
#include <StreamUtils.h>
#include "SerialCommunication.hpp"
#include "Utils.hpp"

const char* _prova = "{state: \"state\", data: 0}";
// {"messageID":2, "data":-1}
// {"messageID":1, "data":40}
// {"messageID":1, "data":50}
// {"messageID":0, "data":-1}

void prova() {
    Serial.println(_prova);
}

void interpretMessage(int valveOpeningLevel, riverControllerStates arduinoState, SensorsController &controller) 
{
    valveOpeningLevel = mapFromAngleToPercentage(valveOpeningLevel);
    if (Serial.available() > 0) {
        JsonDocument doc;
        deserializeJson(doc, Serial);
        /* Previous deserialisations might leave some spaces and/or terminating
           characters on the serial, that would be intercepted by following reads.
           This while prevents this from happening by consuming all space-like
           characters. The peek() method allows to look at the content of
           the serial port without consuming it.
        */
        while (isspace(Serial.peek()))
            Serial.read();
        // DEBUG ONLY
        /* if (error != DeserializationError::Ok) {
            controller.controllerLCDPrint(":(");
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
        } */
        int messageID = doc["messageID"];
        switch (messageID) {
            case 0: // get opening level
                    sendValveMessage(valveOpeningLevel, controller);
                    break;
            case 1: // set opening level
                    /* The value received from the backend is a percentage
                       (a number ranging from 0 to 100) and needs to be mapped
                       to a valve opening angle, in the range [0, 180].
                    */
                    controller.controllerSetGate(mapFromPercentageToAngle(doc["data"]));
                    /* Update the value shown on the LCD */
                    char buffer[sizeof(int)];
                    sprintf(buffer, "%d", mapFromAngleToPercentage(controller.getAngle()));
                    controller.controllerLCDPrint(buffer);
                    /* Send acknowledge message on serial */
                    sendValveMessage(doc["data"], controller);
                    break;
            case 2: // return the state of the Water Channel Controller
                    sendStateMessage(arduinoState, controller);
                    break;
            default: 
                    controller.controllerLCDPrint("ERROR");
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
