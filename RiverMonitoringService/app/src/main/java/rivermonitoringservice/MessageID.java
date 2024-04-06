package rivermonitoringservice;

/**
 * This enum keeps track of the messages IDs used to communicate with the Water Channel Controller.
 * 
 * {@link rivermonitoringservice.MessageID#GET_OPENING_LEVEL} => the Channel Controller must respond 
 * with the current valve opening level percentage.
 * {@link rivermonitoringservice.MessageID#SET_OPENING_LEVEL} => the Channel Controller must set the 
 * valve to the specified opening level, if it is in automatic mode.
 * {@link rivermonitoringservice.MessageID#GET_CONTROLLER_STATE} => the Channel Controller must respond
 * with its current state (automatic or manual).
 */
public enum MessageID {
    GET_OPENING_LEVEL,
    SET_OPENING_LEVEL,
    GET_CONTROLLER_STATE
}
