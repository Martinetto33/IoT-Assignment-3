#include <Arduino.h>
#include <PubSubClient.h>
#include "WiFi-Utilities.h"

WiFiClient espClient;
PubSubClient client(espClient);
extern const char* ssid; // defined in config.h
extern const char* password; // defined in config.h

void setup_wifi() {

  delay(10);

  Serial.println(String("Connecting to ") + ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

bool is_wifi_connected() {
  return WiFi.status() == WL_CONNECTED;
}

