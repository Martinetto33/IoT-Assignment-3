package rivermonitoringservice;

/**
 * This enum keeps track of the messages IDs used to communicate with the Water Channel Controller.
 * 
 * {@link rivermonitoringservice.MessageID#GET_OPENING_LEVEL} => the Channel Controller must respond 
 * with the current valve opening level percentage. No data is sent from the River Monitoring Service
 * (so use Optional.empty() in {@link rivermonitoringservice.serial.SerialCommunicator#writeJsonToSerial(MessageID, java.util.Optional)}).
 * {@link rivermonitoringservice.MessageID#SET_OPENING_LEVEL} => the Channel Controller must set the 
 * valve to the specified opening level, if it is in automatic mode. The Optional will contain the new
 * opening level percentage.
 * {@link rivermonitoringservice.MessageID#GET_CONTROLLER_STATE} => the Channel Controller must respond
 * with its current state (automatic or manual). No data is sent from the River Monitoring Service,
 * so use and empty Optional.
 */
public enum MessageID {
    GET_OPENING_LEVEL,
    SET_OPENING_LEVEL,
    GET_CONTROLLER_STATE
}
