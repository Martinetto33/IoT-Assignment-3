package rivermonitoringservice.serial;

import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import rivermonitoringservice.MessageID;

public class SerialCommunicator implements SerialPortEventListener {
    private SerialPort serialPort;
    //private SerialParser serialParser;
    private ChannelControllerAnswerMessage receivedData; // the valve opening level percentage OR the state of the Water Channel Controller (auto or manual)
    private volatile boolean hasMessageArrived;

    public SerialCommunicator() {
        this.hasMessageArrived = false;
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent event) {
        if(event.getEventType() == SerialPort.MASK_RXCHAR && event.getEventValue() > 0) {
            try {
                String receivedData = serialPort.readString(event.getEventValue());
                //System.out.print(receivedData);
                //progressBar.setValue(Integer.parseInt(progress.replaceAll("(prog: |\\r|\\n)", "")));
                //final String parsedString = this.serialParser.parseReceivedMessage(receivedData);
                //this.receivedData = Integer.parseInt(parsedString.replaceAll("(\"|\\r|\\n)", ""));
                ObjectMapper mapper = new ObjectMapper();
                this.receivedData = mapper.readValue(receivedData, ChannelControllerAnswerMessage.class);
                this.hasMessageArrived = true;
                System.out.println("Got the following message: " + this.receivedData);
            }
            catch (SerialPortException ex) {
                System.out.println("Serial error in receiving string from COM-port: " + ex);
            } catch (JsonMappingException e) {
                System.out.println("Error while mapping received Json data: " + receivedData);
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                System.out.println("Error while processing received Json data: " + receivedData);
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (SerialPortList.getPortNames().length == 0) {
            System.out.println("No serial communication detected; maybe connect your Arduino?");
            return;
        }
        this.serialPort = new SerialPort(SerialPortList.getPortNames()[0]);
        //this.serialParser = new SerialParser();
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                                          SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);                   
        } catch (SerialPortException ex) {
            System.out.println("Error during the initialisation of the SerialCommunicator: " + ex);
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
        try {
            this.serialPort.writeString(message);
        } catch (SerialPortException e) {
            System.out.println("Error while trying to write << " + message
                                + " >> to serial port!");
            e.printStackTrace();
        }
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
        while (System.currentTimeMillis() - start < timeInterval && !this.hasMessageArrived) {}
        if (!this.hasMessageArrived) {
            throw new NoMessageArrivedException();
        }
    }

    public boolean hasMessageArrived() {
        return this.hasMessageArrived;
    }

    public ChannelControllerAnswerMessage getReceivedData() {
        this.hasMessageArrived = false;
        return this.receivedData;
    }
}
