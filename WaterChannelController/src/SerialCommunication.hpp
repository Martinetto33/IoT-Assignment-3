#pragma once
#include "RiverControllerFSM.hpp"
#include "Sensors/SensorsController.hpp"

void interpretMessage(int valveOpeningLevel, riverControllerStates state, SensorsController &controller);

void sendStateMessage(int arduinoStateAsInt, SensorsController &controller);

void sendValveMessage(int valveOpeningLevel, SensorsController &controller);