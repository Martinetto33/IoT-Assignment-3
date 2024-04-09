package rivermonitoringservice.serial;

import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jssc.SerialPort;
import jssc.SerialPortList;
import rivermonitoringservice.MessageID;
import rivermonitoringservice.serial.professor.CommChannel;
import rivermonitoringservice.serial.professor.SerialCommChannel;

public class SerialCommunicator {
    private CommChannel commChannel;
    private ChannelControllerAnswerMessage receivedData; // the valve opening level percentage OR the state of the Water Channel Controller (auto or manual)

    public SerialCommunicator() {}

    public ChannelControllerAnswerMessage getReceivedData() {
        try {
            String serialData = this.commChannel.receiveMsg();
            ObjectMapper mapper = new ObjectMapper();
            this.receivedData = mapper.readValue(serialData, ChannelControllerAnswerMessage.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(4);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            System.exit(5);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.exit(6);
        } 
        System.out.println("Got the following message: " + this.receivedData);
        return this.receivedData;
    }

    public void start() {
        if (SerialPortList.getPortNames().length == 0) {
            System.out.println("No serial communication detected; maybe connect your Arduino?");
            return;
        }
        try {
            this.commChannel = new SerialCommChannel(SerialPortList.getPortNames()[0], SerialPort.BAUDRATE_9600);
        } catch (Exception e) {
            System.out.println("Error while creating professor's CommChannel.");
            e.printStackTrace();
        }
    }
/* 
    public void writeToSerial(final int openingLevel) {
        try {
            this.serialPort.writeInt(openingLevel);
        } catch (SerialPortException e) {
            System.out.println("Error while writing on serial port.");
        }
    } */

    private void writeToSerial(final String message) {
        this.commChannel.sendMsg(message);
    }

    public void writeJsonToSerial(final MessageID messageID, final Optional<Integer> data) {
        final ChannelControllerQueryMessage msg = new ChannelControllerQueryMessage(messageID, data);
        try {
            String result = new ObjectMapper().writeValueAsString(msg);
            System.out.println("Successfully created JSON object: " + result);
            this.writeToSerial("result");
        } catch (JsonProcessingException e) {
            System.out.println("Error while trying to write JSON object!");
            e.printStackTrace();
        }
    }

    /**
     * Waits for a serial event to occur. Try to avoid using this method,
     * since it does busy waiting.
     * @param timeInterval the maximum time interval to be waited, expressed in milliseconds.
     */
    public void waitForSerialCommunication(long timeInterval) throws NoMessageArrivedException {
        final long start = System.currentTimeMillis();
        /* Wait for a message to arrive. This is busy waiting,
         * so the system should not spend much time here.
         */
        while (System.currentTimeMillis() - start < timeInterval && !this.commChannel.isMsgAvailable()) {}
        if (!this.commChannel.isMsgAvailable()) {
            throw new NoMessageArrivedException();
        }
    }

    public boolean hasMessageArrived() {
        return this.commChannel.isMsgAvailable();
    }
}
