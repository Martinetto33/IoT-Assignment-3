#include "Mqtt-Utilities.h"
//#include "config.h"

extern PubSubClient client;
extern const char* mqtt_server;
extern const char* topic;

void callback(char* topicIn, byte* payload, unsigned int length) {
    Serial.println(String("Message arrived on [") + topicIn + "] len: " + length);
}

void setup_mqtt() {
    client.setServer(mqtt_server, 1883);
    client.setCallback(callback);
}

void mqtt_publish(const char* msg) {
    client.publish(topic, msg);
}

bool mqtt_is_client_connected() {
    return client.connected();
}

void mqtt_reconnect() {
  
  // Loop until we're reconnected
  
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    
    // Create a random client ID
    String clientId = String("esiot-2122-client-")+String(random(0xffff), HEX);

    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      // client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe(topic);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void mqtt_client_loop() {
    client.loop();
}