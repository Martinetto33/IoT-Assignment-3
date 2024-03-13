#include "DataSenderTask.hpp"
#define MESSAGE_BYTES 20

DataSenderTask::DataSenderTask() {
    this->canExecute = true;
}

void DataSenderTask::sendData(float data) {
    if (mqtt_is_client_connected()) {
        /* The float needs to be converted to a string.
        See: http://www.steves-internet-guide.com/send-and-receive-integers-and-floats-with-arduino-over-mqtt/
        */
        char messageOutput[MESSAGE_BYTES];
        /* dtostrf() signature:
           - 1st argument: the float to convert to C string
           - 2nd argument: the minimum number of digits before the decimal point
           - 3rd argument: the precision specified as the number of digits after the decimal point
           - 4th argument: the output buffer
        */
        dtostrf(data, 1, 3, messageOutput);
        mqtt_publish(messageOutput);
    }
}

void DataSenderTask::pause() {
    this->canExecute = false;
}

void DataSenderTask::resume() {
    this->canExecute = true;
}