#pragma once
#include <PubSubClient.h>

void setup_mqtt();

void mqtt_publish(const char* msg);

bool mqtt_is_client_connected();

void mqtt_reconnect();

void mqtt_client_loop();