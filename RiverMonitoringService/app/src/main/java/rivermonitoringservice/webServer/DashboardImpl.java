package rivermonitoringservice.webServer;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rivermonitoringservice.RiverMonitoringService;
import rivermonitoringservice.SharedMemory.SharedMemory;

@RestController
public class DashboardImpl implements Dashboard {
    /* UPDATE: these fields need to be static for Spring to detect them. 
     * NOTE: these fields represent the ACTUAL values contained in the shared memory,
     * meaning these are the date actually measured by the physical sensors.
     * The field 'requestedOpeningLevel' instead contains the gate opening level
     * wanted by the remote user interacting with the web application.
    */
	private static double waterLevel = 0.0;		        //indicates the height of the water
	private static int openingGatePercentage = 0;	//indicates the opening of the valve %
	private static String status = "a";			//indicates the status of the system
    private static String suggestedValveOpeningLevel = "";

    /* These fields are used to convey a remote user's commands to the backend.
     * When a new opening level is requested, the flag is set to true and the
     * requestedOpeningLevel variable will store the wanted opening level.
     */
    private volatile int requestedOpeningLevel = 0;
    private volatile boolean isValveChangeRequested = false;

    public synchronized void refreshDashboardWithDataFromSharedMemory(final SharedMemory shMemory) {
        waterLevel = shMemory.getWaterLevel();
        openingGatePercentage = shMemory.getOpeningGatePercentage();
        status = shMemory.getStatus();
        suggestedValveOpeningLevel = shMemory.getSuggestedValveOpeningLevel();
    }

    public boolean wasNewOpeningLevelRequested() {
        return this.isValveChangeRequested;
    }

    public void notifyThatRequestWasHandled() {
        if (this.isValveChangeRequested) {
            this.isValveChangeRequested = false;
        }
    }

    public Optional<Integer> getUserRequestedOpeningLevel() {
        return this.isValveChangeRequested ? Optional.of(this.requestedOpeningLevel) : Optional.empty();
    }

    // TODO: does this method need to return an integer?
    @CrossOrigin(origins = "*")
    @PostMapping("/dashboard")
    public synchronized int requestOpeningLevelFromWebApp(@RequestParam(value = "level")int gateOpening) {
        try {
            this.requestedOpeningLevel = gateOpening;
            this.isValveChangeRequested = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RiverMonitoringService.serviceSharedMemory().getOpeningGatePercentage();
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getLevel")
    public synchronized double getLevel() {
        return DashboardImpl.waterLevel;
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getStatus")
    public String getStatus() {
        return DashboardImpl.status;
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getGatePercentage")
    public int getOpening() {
        return DashboardImpl.openingGatePercentage;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getSuggestedOpeningLevel")
    public String getSuggestedOpeningLevel() {
        return DashboardImpl.suggestedValveOpeningLevel;
    }
}