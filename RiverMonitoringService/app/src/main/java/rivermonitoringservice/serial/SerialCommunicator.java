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
    private String jsonString;

    public SerialCommunicator() {}

    public ChannelControllerAnswerMessage getReceivedData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.receivedData = mapper.readValue(this.jsonString, ChannelControllerAnswerMessage.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            System.exit(4);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.exit(5);
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
            this.writeToSerial(result);
        } catch (JsonProcessingException e) {
            System.out.println("Error while trying to write JSON object!");
            e.printStackTrace();
        }
    }

    public String waitForSerialCommunication() throws NoMessageArrivedException {
        try {
            this.jsonString = this.commChannel.receiveMsg();
            return this.jsonString;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new NoMessageArrivedException();
        }
    }

    public boolean hasMessageArrived() {
        return this.commChannel.isMsgAvailable();
    }
}
