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

/**
 * FreeRTOS documentation at:
 * https://www.freertos.org/RTOS-task-notifications.html
 * 
 * Tasks were synchronised following this guide:
 * https://microcontrollerslab.com/freertos-binary-semaphore-tasks-interrupt-synchronization-u-arduino/
 * 
 * It's important to add a delay to every task in order for them to be able to reset their watchdog timer;
 * this is why in the for loop of each task there's a call to vTaskDelay(). This should also be done to allow
 * the IDLE task, which has the lowest priority, to do its job and to reset its own watchdog timer.
 * Source: https://stackoverflow.com/questions/66278271/task-watchdog-got-triggered-the-tasks-did-not-reset-the-watchdog-in-time
 * 
 * See more about the IDLE Task in FreeRTOS library in the official guide:
 * https://www.freertos.org/RTOS-idle-task.html
*/

/** The water level is intended as the distance of the water surface from the sonar;
 * if it's close to 0.0, it means that the water has risen to the same level as the sensor,
 * with disastrous consequences.
*/
volatile float water_level = 10.0; // a fake value just to initialise the system in a safe state
volatile int measurement_frequency = 20; // frequency expressed in number of measurements per second

/* This is the equivalent of a mutex; a Task reading or modifying the 
water_level or the measurement_frequency variable should first acquire the semaphore. */
SemaphoreHandle_t waterLevelSemaphore;
SemaphoreHandle_t mFrequencySemaphore;

TaskHandle_t connectionMonitoringTask;
TaskHandle_t dataSenderTask;
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
    vTaskDelay(1);
  }
}

/**
 * Keeps sending the measured water level to the MQTT server.
*/
void dataSenderTaskCode(void* parameter) {
  DataSenderTask* task = new DataSenderTask();
  float local_measurement_frequency;
  for (;;) {
    if (fsm.getState() == NETWORK_OK) {
      task->resume();
      float local_water_level;
      xSemaphoreTake(waterLevelSemaphore, portMAX_DELAY);
      /* When this task gains access to the guarded variable by acquiring
      the mutex, read the water level. */
      local_water_level = water_level;
      xSemaphoreGive(waterLevelSemaphore);
      Serial.println(String("Read water level: ") + water_level);
      task->sendData(local_water_level);

      /* The data sender task should send data at the same pace of the measurement
      of the frequency. */
      xSemaphoreTake(waterLevelSemaphore, portMAX_DELAY);
      /* When this task gains access to the guarded variable by acquiring
      the mutex, read the measurement frequency. */
      local_measurement_frequency = measurement_frequency;
      xSemaphoreGive(waterLevelSemaphore);
      delay(1000 / local_measurement_frequency); // wait for the measurement period to elapse
    } else {
      task->pause(); // wait for someone to re-establish the connection
    }
  }
}

void sonarMeasTaskCode(void* parameters) {
  SonarMeasurementTask* task = new SonarMeasurementTask();
  float local_measurement_frequency;
  for(;;) {
    task->measureWaterLevel();
    if (fsm.getState() == NETWORK_OK && task->isMeasurementAvailable()) {
      xSemaphoreTake(waterLevelSemaphore, portMAX_DELAY);
      /* When this task gains access to the guarded variable by acquiring
      the mutex, update the water level. */
      water_level = task->getLastMeasurement();
      xSemaphoreGive(waterLevelSemaphore);
      Serial.println(String("Water level: ") + water_level);
    }
    xSemaphoreTake(waterLevelSemaphore, portMAX_DELAY);
    /* When this task gains access to the guarded variable by acquiring
    the mutex, read the measurement frequency. */
    local_measurement_frequency = measurement_frequency;
    xSemaphoreGive(waterLevelSemaphore);
    delay(1000 / local_measurement_frequency); // wait for the measurement period to elapse
  }
}

void setup() {
  Serial.begin(115200);
  setup_wifi();
  setup_mqtt();

  waterLevelSemaphore = xSemaphoreCreateBinary();
  mFrequencySemaphore = xSemaphoreCreateBinary();
  xTaskCreatePinnedToCore(connectMonitTaskCode, "ConnectionMonitoringTask", 10000, NULL, 1, &connectionMonitoringTask, 0);                         
  delay(500);
  xTaskCreatePinnedToCore(dataSenderTaskCode, "DataSenderTask", 10000, NULL, 1, &dataSenderTask, 1);                     
  delay(500);
  xTaskCreatePinnedToCore(sonarMeasTaskCode, "SonarMeasurementTask", 10000, NULL, 1, &sonarMeasurementTask, 1);                         
  delay(500);
  xSemaphoreGive(waterLevelSemaphore);
  xSemaphoreGive(mFrequencySemaphore);
}

void loop() {}