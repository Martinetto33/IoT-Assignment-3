package rivermonitoringservice.serial;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import rivermonitoringservice.WaterChannelControllerState;

/**
 * A class representing a message exchanged through serial port between the River Monitoring
 * Service and the Water Channel Controller. These messages represent the Channel Controller
 * answers to requests from the backend.
 * 
 * Note: ALWAYS check the type of the received message before reading the data, since two
 * different methods are provided for the latter.
 */
public class ChannelControllerAnswerMessage {
    public static final String MESSAGE_TYPE_CONTROLLER_STATUS = "status";
    public static final String MESSAGE_TYPE_VALVE_LEVEL = "valve";
    private final String messageType;
    private final int data;

    @JsonCreator
    public ChannelControllerAnswerMessage(@JsonProperty("messageType") final String messageType, @JsonProperty("data") final int data) {
        this.messageType = messageType;
        this.data = data;
    }

    public String getMessageType() {
        if (!MESSAGE_TYPE_CONTROLLER_STATUS.equals(this.messageType) && !MESSAGE_TYPE_VALVE_LEVEL.equals(this.messageType)) {
            System.out.println("Unrecognised message type received from Water Channel Controller: " + this.messageType);
        }
        return this.messageType;
    }

    public int getDataAsValveOpeningLevel() {
        return this.data;
    }

    public WaterChannelControllerState getDataAsArduinoState() {
        return this.data == 0 ? WaterChannelControllerState.AUTO :
               this.data == 1 ? WaterChannelControllerState.MANUAL :
               WaterChannelControllerState.UNINITIALISED;
    }

    public String toString() {
        return new StringBuilder()
                .append("WC Controller message = [messageType: ")
                .append(this.messageType)
                .append(", data: ")
                .append(this.data)
                .append("]")
                .toString();
    }
}
