package rivermonitoringservice.channelcontroller;

import java.util.Optional;

import com.google.common.collect.Range;

import rivermonitoringservice.MessageID;
import rivermonitoringservice.WaterChannelControllerState;
import rivermonitoringservice.SharedMemory.SharedMemory;
import rivermonitoringservice.serial.ChannelControllerAnswerMessage;
import rivermonitoringservice.serial.SerialCommunicator;

public class WaterChannelController {
    private final SharedMemory shMemory;
    private final SerialCommunicator serialCommunicator;
    private WaterChannelControllerState waterChannelControllerState;

    public WaterChannelController(final SharedMemory sharedMemory) {
        this.shMemory = sharedMemory;
        this.serialCommunicator = new SerialCommunicator();
        this.serialCommunicator.start();
        /* The serial communicator scans for a COM port; if successful,
         * this constructor attempts to get the initial state and valve
         * opening level of the physical Water Channel Controller.
         */
        this.blockingSendRecvAndHandle(MessageID.GET_CONTROLLER_STATE, Optional.empty());
        if (this.waterChannelControllerState == WaterChannelControllerState.UNINITIALISED) {
            System.out.println("No initial state of the physical Water Channel Controller was received" +
                                 " in the constructor of the software WaterChannelController.");
            System.exit(7);
        }
        this.blockingSendRecvAndHandle(MessageID.GET_OPENING_LEVEL, Optional.empty());
    }

    /**
     * Getter of the state of the physical Water Channel Controller (automatic
     * or manual).
     * @return a {@link rivermonitoringservice.WaterChannelControllerState} describing
     * the state of the physical Water Channel Controller.
     */
    public WaterChannelControllerState getState() {
        this.blockingSendRecvAndHandle(MessageID.GET_CONTROLLER_STATE, Optional.empty());
        return this.waterChannelControllerState;
    }

    /**
     * Asks the physical Water Channel Controller the current opening level
     * percentage of the valve. It also modifies the shared memory by writing the
     * read value.
     * @return the opening level percentage of the physical valve.
     */
    public int askForValveOpeningLevelPercentage() {
        this.blockingSendRecvAndHandle(MessageID.GET_OPENING_LEVEL, Optional.empty());
        return this.shMemory.getOpeningGatePercentage();
    }

    /**
     * Commands the physical Water Channel Controller to open the valve to a certain
     * level. The new opening level is written to the shared memory.
     * @param openingLevelPercentage the new opening level percentage.
     */
    public void requestControllerToSetValveOpeningLevel(final int openingLevelPercentage) {
        if (!Range.closed(0, 100).contains(openingLevelPercentage)) {
            System.out.println("Invalid request to set the physical valve to the " + 
                                "following opening level percentage: " + openingLevelPercentage);
            System.exit(6);
        }
        /* Change the physical valve only if the required opening level is different from the
         * current one.
         */
        if (this.shMemory.getOpeningGatePercentage() != openingLevelPercentage) {
            this.blockingSendRecvAndHandle(MessageID.SET_OPENING_LEVEL, Optional.of(openingLevelPercentage));
        }
    }

    /**
     * This method MUST stay private to ensure encapsulation and separation of concerns.
     * Performs a blocking (synchronous) communication with the physical Water Channel
     * Controller, by sending a message, waiting for the response, handling the response
     * and optionally setting the involved variables in the shared memory.
     * @param messageID the {@link rivermonitoringservice.MessageID} to send
     * @param openingLevelRequestedByDashboard an optional parameter containing the
     * valve opening level percentage requested by the remote user through the dashboard
     */
    private void blockingSendRecvAndHandle(final MessageID messageID, 
                                final Optional<Integer> openingLevelRequestedByDashboard) {
        this.serialCommunicator.writeJsonToSerial(messageID, openingLevelRequestedByDashboard);
        this.serialCommunicator.waitForSerialCommunication();
        this.handleWCCCommunications();
    }

    /**
     * If the received message was the Water Channel Controller state, this method
     * modifies the local field 'waterChannelControllerState'. Otherwise, if the
     * current physical valve opening level was requested, this method sets it
     * to its actual value in the shared memory.
     */
    private void handleWCCCommunications() {
        if (this.serialCommunicator.hasMessageArrived()) {
            final ChannelControllerAnswerMessage msg = serialCommunicator.getReceivedData();
            if (ChannelControllerAnswerMessage.MESSAGE_TYPE_CONTROLLER_STATUS.equals(msg.getMessageType())) {
                this.waterChannelControllerState = msg.getDataAsArduinoState();
            } else if (ChannelControllerAnswerMessage.MESSAGE_TYPE_VALVE_LEVEL.equals(msg.getMessageType())) {
                this.shMemory.setOpeningGatePercentage(msg.getDataAsValveOpeningLevel());
            }
        }
    }
}
