/* 
  ASSIGNMENT 3 for the "Embedded Systems and Internet Of Things" course, 2023

  Working group:
  - Bordeianu Alin Stefan
  - Lizza Alessio
  - Rettori Lucrezia
*/
#include <Arduino.h>
#include "RiverControllerTask.hpp"

Task* task;

void setup() {
  Serial.begin(9600);
  task = new RiverControllerTask();
}

void loop() {
  task->tick(100);
}