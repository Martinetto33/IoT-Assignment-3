#include <PubSubClient.h>
#include "config.h"
#include "WiFi-Module/WiFi-Utilities.h"
#include "WiFI-Module/Mqtt-Utilities.h"
#include "FreeRtosTasks/NetworkStatusFSM.hpp"
#include "FreeRtosTasks/ConnectionMonitoringTask.hpp"
#include "FreeRtosTasks/DataSenderTask.hpp"
#include "FreeRtosTasks/SonarMeasurementTask.hpp"
#include "Sensors/Led.hpp"
#define MSG_BUFFER_SIZE 50

/** The water level is intended as the distance of the water surface from the sonar;
 * if it's close to 0.0, it means that the water has risen to the same level as the sensor,
 * which is a great problem.
*/
float water_level;
int measurement_frequency = 20; // frequency expressed in number of measurements per second

TaskHandle_t connectionMonitoringTask;
TaskHandle_t dataSenderTask;
TaskHandle_t frequencyReceiverTask; // see below
TaskHandle_t sonarMeasurementTask;

NetworkStatusFSM fsm;

/** 
 * Continuously polls the state of the network, but this code should be managed by the FreeRTOS library
 * so this approach should not be as detrimental as usual.
*/
void connectMonitTaskCode(void* parameter) {
  ConnectionMonitoringTask* task = new ConnectionMonitoringTask();
  Led* greenLed = new Led(GREEN_LED_PIN);
  Led* redLed = new Led(RED_LED_PIN);

  for(;;) { 
    if (!task->isConnectionOk()) {
      fsm.setState(NETWORK_ERROR);
      greenLed->turnOff();
      redLed->turnOn();
      task->reconnect();
    } else {
      fsm.setState(NETWORK_OK);
      greenLed->turnOn();
      redLed->turnOff();
    }
  }
}

/**
 * Keeps sending the measured water level to the MQTT server.
*/
void dataSenderTaskCode(void* parameter) {
  DataSenderTask* task = new DataSenderTask();
  for (;;) {
    if (fsm.getState() == NETWORK_OK) {
      task->resume();
      task->sendData(water_level);
    } else {
      task->pause(); // wait for someone to re-establish the connection
    }
  }
}

/* TODO: the frequency receiver task might be useless, since the mqtt client modifies
the global variable. See Mqtt-Utilities.cpp for further details, in the "callback()" function.
*/

void sonarMeasTaskCode(void* parameters) {
  SonarMeasurementTask* task = new SonarMeasurementTask();
  for(;;) {
    task->measureWaterLevel();
    if (fsm.getState() == NETWORK_OK && task->isMeasurementAvailable()) {
      water_level = task->getLastMeasurement();
      Serial.println(String("Water level: ") + water_level);
    }
    delay(1000 / measurement_frequency); // wait for the measurement period to elapse
  }
}

void setup() {
  Serial.begin(115200);
  setup_wifi();
  setup_mqtt();

  xTaskCreatePinnedToCore(connectMonitTaskCode, "ConnectionMonitoringTask", 10000, NULL, 1, &connectionMonitoringTask, 0);                         
  delay(500);
  xTaskCreatePinnedToCore(dataSenderTaskCode, "DataSenderTask", 10000, NULL, 1, &dataSenderTask, 1);                     
  delay(500);
  xTaskCreatePinnedToCore(sonarMeasTaskCode, "SonarMeasurementTask", 10000, NULL, 1, &sonarMeasurementTask, 1);                         
  delay(500);
}

void loop() {}