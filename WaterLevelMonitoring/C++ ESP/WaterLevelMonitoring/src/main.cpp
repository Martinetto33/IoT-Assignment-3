#include <PubSubClient.h>
#include "config.h"
#include "WiFi-Module/WiFi-Utilities.h"
#include "WiFI-Module/Mqtt-Utilities.h"
#include "Queue/Queue.hpp"
#define MSG_BUFFER_SIZE  50

unsigned long lastMsgTime = 0;
char msg[MSG_BUFFER_SIZE];
int value = 0;

void setup() {
  Serial.begin(115200);
  setup_wifi();
  setup_mqtt();
  randomSeed(micros());
}

void loop() {

  if (!mqtt_is_client_connected()) {
    mqtt_reconnect();
  }
  mqtt_client_loop();

  unsigned long now = millis();
  if (now - lastMsgTime > 10000) {
    lastMsgTime = now;
    value++;

    /* creating a msg in the buffer */
    snprintf (msg, MSG_BUFFER_SIZE, "hello world #%ld", value);

    Serial.println(String("Publishing message: ") + msg);
    
    /* publishing the msg */
    mqtt_publish(msg);  
  }
}