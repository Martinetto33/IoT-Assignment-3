#include <Arduino.h>
#include <PubSubClient.h>
//#include "config.h"
#include "WiFi-Utilities.h"
//#include "Mqtt-Utilities.h"

WiFiClient espClient;
PubSubClient client(espClient);
extern const char* ssid;
extern const char* password;

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

