/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package rivermonitoringservice;

import rivermonitoringservice.mqtt.MqttManager;
import rivermonitoringservice.serial.SerialCommunicator;
import rivermonitoringservice.webServer.RiverMonitoringDashboardApplication;

public class RiverMonitoringService {
    private static final RiverMonitoringDashboardApplication dashboard = new RiverMonitoringDashboardApplication();
    private static final MqttManager mqttServer = new MqttManager();
    private static final SerialCommunicator serialCommunicator = new SerialCommunicator();

    public String getGreeting() {
        return "Hello World!2";
    }

    public static void main(String[] args) {
        setup(args);
        System.out.println(new RiverMonitoringService().getGreeting());
        
    }

    private static void setup(String[] args) {
        RiverMonitoringService.dashboard.startWebServer(args);
        RiverMonitoringService.mqttServer.startMqttServer();
        serialCommunicator.start();
    }
}
