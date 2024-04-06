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
    private final MessageID messageID;
    private final int data;

    public ChannelControllerMessage(final MessageID message, final Optional<Integer> data) {
        this.messageID = message;
        this.data = data.orElse(-1);
    }

    @JsonProperty("messageID")
    public MessageID getMessageID() {
        return this.messageID;
    }

    @JsonProperty("data")
    public int getData() {
        return this.data;
    }
}
