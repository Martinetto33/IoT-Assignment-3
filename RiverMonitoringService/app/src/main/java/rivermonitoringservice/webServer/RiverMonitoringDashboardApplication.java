package rivermonitoringservice.webServer;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiverMonitoringDashboardApplication implements RiverMonitoringDashboardApplicationInterface {
    /* UPDATE: these fields need to be static for Spring to detect them. */
	private static double waterLevel = 0.0;		        //indicates the height of the water
	private static int openingGatePercentage = 0;	//indicates the opening of the valve %
	private static String status = "a";			//indicates the status of the system
    private static String suggestedValveOpeningLevel = "";
    
    public void setWaterLevel(double waterLevel) {
        RiverMonitoringDashboardApplication.waterLevel = waterLevel;
    }
    
    public void setStatus(String status) {
        RiverMonitoringDashboardApplication.status = status;
    }

    public void setSuggestedValveOpeningLevel(String suggestedValveOpeningLevel) {
        RiverMonitoringDashboardApplication.suggestedValveOpeningLevel = suggestedValveOpeningLevel;
    }
    
    @Override
    @CrossOrigin(origins = "*")
    @PostMapping("/dashboard")
    public int dashboard(@RequestParam(value = "level")int gateOpening) {
        try {
            RiverMonitoringDashboardApplication.openingGatePercentage = gateOpening;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RiverMonitoringDashboardApplication.openingGatePercentage;
    }
    
    @Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getLevel")
    public double getLevel() {
        return RiverMonitoringDashboardApplication.waterLevel;
    }
    
	@Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getStatus")
    public String getStatus() {
        return RiverMonitoringDashboardApplication.status;
    }
    
	@Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getGatePercentage")
    public int getOpening() {
        return RiverMonitoringDashboardApplication.openingGatePercentage;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getSuggestedOpeningLevel")
    public String getSuggestedOpeningLevel() {
        return RiverMonitoringDashboardApplication.suggestedValveOpeningLevel;
    }
}