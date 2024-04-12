/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package rivermonitoringservice;

import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import rivermonitoringservice.SharedMemory.SharedMemory;
import rivermonitoringservice.data.RiverMonitoringServiceData;
import rivermonitoringservice.fsm.RiverMonitoringServiceFSM;
import rivermonitoringservice.mqtt.MqttManager;
import rivermonitoringservice.serial.ChannelControllerAnswerMessage;
import rivermonitoringservice.serial.NoMessageArrivedException;
import rivermonitoringservice.serial.SerialCommunicator;
import rivermonitoringservice.state.code.NormalState;
import rivermonitoringservice.webServer.RiverMonitoringDashboardApplication;
import rivermonitoringservice.webServer.RiverMonitoringDashboardApplicationInterface;

@SpringBootApplication
public class RiverMonitoringService {
    private static final RiverMonitoringDashboardApplicationInterface dashboard = new RiverMonitoringDashboardApplication();
    private static final MqttManager mqttServer = new MqttManager();
    private static final SerialCommunicator serialCommunicator = new SerialCommunicator();
    private static final RiverMonitoringServiceFSM fsm = new RiverMonitoringServiceFSM();
    private static final SharedMemory sharedMemory = new SharedMemory();
    private static int valveOpeningLevel = 0; // the valve opening level percentage
    private static WaterChannelControllerState arduinoState = WaterChannelControllerState.UNINITIALISED;

    public String getGreeting() {
        return "Hello World!2";
    }

    public static void main(String[] args) {
        SpringApplication.run(RiverMonitoringService.class, args);
        setup(args);
        System.out.println(new RiverMonitoringService().getGreeting());        
        // TODO: consider improving the dashboard logic; as of now it always sends a non-empty Optional.
        /* Checking and updating the Water Channel Controller state: */
        while (true) {
            serialCommunicator.writeJsonToSerial(MessageID.GET_CONTROLLER_STATE, Optional.empty());
            RiverMonitoringService.handleWCCCommunications();
            if (arduinoState == WaterChannelControllerState.UNINITIALISED) {
                System.out.println("Something wrong occurred while receiving the state of the Water Channel Controller.");
                System.exit(3);
            }
            /* Checking and updating the Water Channel Controller valve opening level. */
            serialCommunicator.writeJsonToSerial(MessageID.GET_OPENING_LEVEL, Optional.empty());
            final RiverMonitoringServiceData data = new RiverMonitoringServiceData(mqttServer.getWaterLevel(),
                                                                                valveOpeningLevel, 
                                                                                Optional.of(dashboard.getOpening()), 
                                                                                arduinoState);
            RiverMonitoringService.updateDashboard(data.waterLevel(), valveOpeningLevel, fsm.getCurrentState().getStateAsString());
            fsm.handle(data);
        }
    }

    private static void setup(String[] args) {
        RiverMonitoringService.mqttServer.startMqttServer();
        RiverMonitoringService.serialCommunicator.start();
        /* Get the current state of the Water Channel Controller; it's supposedly AUTO at the beginning. */
        RiverMonitoringService.serialCommunicator.writeJsonToSerial(MessageID.GET_CONTROLLER_STATE, Optional.empty());
        // await communication from arduino
        try {
            System.out.println("RECEIVED MESSAGE IN STATIC METHOD SETUP: " + RiverMonitoringService.serialCommunicator.waitForSerialCommunication());
        } catch (NoMessageArrivedException e) {
            System.out.println("No initial state of the Water Channel Controller was received in static method " +
                                 "setup. Aborting.");
            e.printStackTrace();
            System.exit(1);
        }
        RiverMonitoringService.setWaterChannelControllerState(RiverMonitoringService.serialCommunicator.getReceivedData().getDataAsArduinoState());
        /* Get the current valve opening level */
        RiverMonitoringService.serialCommunicator.writeJsonToSerial(MessageID.GET_OPENING_LEVEL, Optional.empty());
        // await communication from arduino
        try {
            System.out.println("RECEIVED MESSAGE IN STATIC METHOD SETUP: " + RiverMonitoringService.serialCommunicator.waitForSerialCommunication());
        } catch (NoMessageArrivedException e) {
            System.out.println("No initial opening level of the Water Channel Controller valve was received in static method " +
                                 "setup. Aborting.");
            e.printStackTrace();
            System.exit(2);
        }
        RiverMonitoringService.valveOpeningLevel = RiverMonitoringService.serialCommunicator.getReceivedData().getDataAsValveOpeningLevel();

        RiverMonitoringService.fsm.changeState(new NormalState());
        System.out.println("SETUP COMPLETED!!");
    }

    public static SharedMemory serviceSharedMemory() {
        return RiverMonitoringService.sharedMemory;
    }

    /**
     * Static method used for serial communication with Water Channel Controller.
     * @param messageID the ID of the message; different messages are handled in different ways,
     * as specified in the {@link rivermonitoringservice.MessageID} class.
     * @param data an Optional containing the requested valve opening level, if needed.
     */
    public static void updateChannelController(final MessageID messageID, final Optional<Integer> data) {
        RiverMonitoringService.serialCommunicator.writeJsonToSerial(messageID, data);
    }

    public static void updateDashboard(final double waterLevel, final int valveOpeningPercentage, final String currentState) {
        /* 
         * PROBLEM: the dashboard interface doesn't offer methods to set the valve opening percentage.
         * My idea would be to add a field in the html page seen by the user with a suggested opening
         * level percentage, so as to allow the backend to communicate the optimal valve opening
         * levels based on the state of the system.
         */
        // TODO: use shared memory if we opt to do so
        RiverMonitoringService.sharedMemory.setWaterLevel(waterLevel);
        dashboard.setWaterLevel(waterLevel);
        RiverMonitoringService.sharedMemory.setSuggestedValveOpeningLevel(String.valueOf(valveOpeningPercentage));
        dashboard.setSuggestedValveOpeningLevel(String.valueOf(valveOpeningPercentage));
        RiverMonitoringService.sharedMemory.setStatus(currentState);
        dashboard.setStatus(currentState);
    }

    public static void updateESPMonitoringSystem(final int mFrequency) {
        mqttServer.communicateNewMeasurementFrequency(mFrequency);
    }

    public static WaterChannelControllerState getWaterChannelControllerState() {
        return RiverMonitoringService.arduinoState;
    }

    private static void handleWCCCommunications() {
        if (serialCommunicator.hasMessageArrived()) {
            final ChannelControllerAnswerMessage msg = serialCommunicator.getReceivedData();
            if (ChannelControllerAnswerMessage.MESSAGE_TYPE_CONTROLLER_STATUS.equals(msg.getMessageType())) {
                setWaterChannelControllerState(msg.getDataAsArduinoState());
            } else if (ChannelControllerAnswerMessage.MESSAGE_TYPE_VALVE_LEVEL.equals(msg.getMessageType())) {
                valveOpeningLevel = msg.getDataAsValveOpeningLevel();
            }
        }
    }

    private static void setWaterChannelControllerState(final WaterChannelControllerState state) {
        RiverMonitoringService.arduinoState = state;
    }

}
