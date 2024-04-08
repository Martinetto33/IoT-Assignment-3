package rivermonitoringservice.serial;

import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonProperty;
import rivermonitoringservice.MessageID;

/**
 * A class representing a message exchanged through serial port between the River Monitoring
 * Service and the Water Channel Controller.
 * 
 * This class should be easy to serialize into JSON objects.
 * 
 * The MessageID enum cand be extended if needed. See {@link rivermonitoringservice.serial.MessageID}
 * for further details.
 */
public class ChannelControllerMessage {
    private final int messageID;
    private final int data;

    public ChannelControllerMessage(final MessageID message, final Optional<Integer> data) {
        switch(message) {
            case GET_CONTROLLER_STATE:
                this.messageID = 2; 
                break;
            case GET_OPENING_LEVEL:
                this.messageID = 0;
                break;
            case SET_OPENING_LEVEL:
                this.messageID = 1;
                break;
            default:
                this.messageID = -1;
                break;
        }
        this.data = data.orElse(-1);
    }

    @JsonProperty("messageID")
    public int getMessageID() {
        return this.messageID;
    }

    @JsonProperty("data")
    public int getData() {
        return this.data;
    }
}
