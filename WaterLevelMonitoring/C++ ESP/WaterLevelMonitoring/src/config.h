/* Use this configuration file to setup ESP32 hardware and WiFi settings.
For this module to work, you will need to replace the WiFi SSID (the name of
your personal WiFi network) and the WiFi password with your actual data. */

#ifndef __WLM_CONFIG_H__
#define __WLM_CONFIG_H__

/* Pins configuration for ESP32 */
int GREEN_LED_PIN = 4;
int RED_LED_PIN = 5;
int SONAR_PIN_ECHO = 9; // the input pin of the sonar
int SONAR_PIN_TRIG = 10; // the output pin of the sonar

/* WiFi network info, replace with actual values.
REMEMBER NOT TO COMMIT THEM TO ONLINE REPOSITORY!!!!
 */
const char* ssid = "Martin Router King";
const char* password = "Martynnn33";

/* MQTT server address, DO NOT change this value. */
const char* mqtt_server = "broker.hivemq.com";

/* MQTT water_level_publication_topic, DO NOT change this value. */
const char* water_level_publication_topic = "esiot-2024/group-4/water-level";
/* MQTT measurement_frequency_subscription_topic, DO NOT change this value. */
const char* measurement_frequency_subscription_topic = "esiot-2024/group-4/measurement-frequency";

#endif