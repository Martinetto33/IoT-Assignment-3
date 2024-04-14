package rivermonitoringservice.webServer;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	private static volatile int openingGatePercentage = 0;	//indicates the opening of the valve %
	private static String status = "a";			//indicates the status of the system
    private static String suggestedValveOpeningLevel = "";

    /* These fields are used to convey a remote user's commands to the backend.
     * When a new opening level is requested, the flag is set to true and the
     * requestedOpeningLevel variable will store the wanted opening level.
     * 
     * The volatile keyword instructs the threads to read the value of the variable
     * ALWAYS from the central memory, and not from the "personal" memories of the
     * single threads. This implies that all threads accessing these variables
     * see the same value at any given point in time.
     * 
     * In our case, we need this keyword because the Spring framework creates
     * personal copies of the objects instantiated by our program, alongside
     * with many threads. Since this class can be accessed by many different
     * threads in an asynchronous way, it is crucial that those threads see
     * the same values when updating the dashboard.
     * 
     * The fields are also declared as static, so that there is only one copy of
     * them shared among all the 'DashboardImpl' instances. This was done to avoid
     * an annoying problem occurring when the user moved the slider of the dashboard
     * to control the valve. The problem was that only the instance of DashboardImpl
     * owned by Spring was updated, and not the one used by the main class
     * RiverMonitoringService.
     * 
     * For a more detailed resume on the keyword volatile, see the following link:
     * https://www.geeksforgeeks.org/volatile-keyword-in-java/
     */
    private static volatile int requestedOpeningLevel = 0;
    private static volatile boolean isValveChangeRequested = false;

    /**
     * Allows only one thread per class (so only one thread in general)
     * to read the value "isValveChangeRequested".
     * @return
     */
    private static synchronized boolean readUserRequestedData() {
        return DashboardImpl.isValveChangeRequested;
    }

    /* INTERFACE METHODS */

    public void refreshDashboardWithDataFromSharedMemory(final SharedMemory shMemory) {
        waterLevel = shMemory.getWaterLevel();
        openingGatePercentage = shMemory.getOpeningGatePercentage();
        status = shMemory.getStatus();
        suggestedValveOpeningLevel = shMemory.getSuggestedValveOpeningLevel();
    }

    public boolean wasNewOpeningLevelRequested() {
        return readUserRequestedData();
    }

    public void notifyThatRequestWasHandled() {
        if (isValveChangeRequested) {
            isValveChangeRequested = false;
        }
    }

    public Optional<Integer> getUserRequestedOpeningLevel() {
        return readUserRequestedData() ? Optional.of(requestedOpeningLevel) : Optional.empty();
    }

    /* SPRING METHODS */

    // TODO: does this method need to return an integer?
    @CrossOrigin(origins = "*")
    @PostMapping("/dashboard")
    public static synchronized int requestOpeningLevelFromWebApp(@RequestParam(value = "level")int gateOpening) {
        try {
            requestedOpeningLevel = gateOpening;
            isValveChangeRequested = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestedOpeningLevel; //RiverMonitoringService.serviceSharedMemory().getOpeningGatePercentage();
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getLevel")
    public double getLevel() {
        return DashboardImpl.waterLevel;
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getStatus")
    public String getStatus() {
        return DashboardImpl.status;
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getGatePercentage")
    public static synchronized int getOpening() {
        return DashboardImpl.openingGatePercentage;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getSuggestedOpeningLevel")
    public String getSuggestedOpeningLevel() {
        return DashboardImpl.suggestedValveOpeningLevel;
    }
}