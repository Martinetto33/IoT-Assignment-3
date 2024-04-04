package rivermonitoringservice.serial;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialCommunicator implements SerialPortEventListener {
    private SerialPort serialPort;
    private SerialParser serialParser;
    private int openingLevel = 0; // the valve opening level percentage

    public SerialCommunicator() {}

    @Override
    public synchronized void serialEvent(SerialPortEvent event) {
        if(event.getEventType() == SerialPort.MASK_RXCHAR && event.getEventValue() > 0) {
            try {
                String receivedData = serialPort.readString(event.getEventValue());
                //System.out.print(receivedData);
                //progressBar.setValue(Integer.parseInt(progress.replaceAll("(prog: |\\r|\\n)", "")));
                final String parsedString = this.serialParser.parseReceivedMessage(receivedData);
                this.openingLevel = Integer.parseInt(parsedString.replaceAll("(\\r|\\n)", ""));
                System.out.println("Got the following opening level: " + this.openingLevel);
            }
            catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }

    public void start() {
        if (SerialPortList.getPortNames().length == 0) {
            System.out.println("No serial communication detected; maybe connect your Arduino?");
            return;
        }
        this.serialPort = new SerialPort(SerialPortList.getPortNames()[0]);
        this.serialParser = new SerialParser();
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

    public void writeToSerial(final int openingLevel) {
        try {
            this.serialPort.writeInt(openingLevel);
        } catch (SerialPortException e) {
            System.out.println("Error while writing on serial port.");
        }
    }

    public int getOpeningLevel() {
        return this.openingLevel;
    }
}
