package rivermonitoringservice.webServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RiverMonitoringDashboardApplication implements RiverMonitoringDashboardApplicationInterface {
	private double waterLevel = 0;		        //indicates the height of the water
	private int openingGatePercentage = 0;	//indicates the opening of the valve %
	private String status = "a";			//indicates the status of the system
    private String suggestedValveOpeningLevel = "";
    
    public void startWebServer(String[] args) {
        SpringApplication.run(RiverMonitoringDashboardApplication.class, args);
    }
    
    @Override
    @CrossOrigin(origins = "*")
    @PostMapping("/dashboard")
    public int dashboard(@RequestParam(value = "level")int gateOpening) {
        try {
			this.openingGatePercentage = gateOpening;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.openingGatePercentage;
    }
    
    @Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getLevel")
    public double getLevel() {
        return waterLevel;
    }
    
	@Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getStatus")
    public String getStatus() {
        return this.status;
    }
    
	@Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getGatePercentage")
    public int getOpening() {
        return this.openingGatePercentage;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }
    
    public void setOpeningGatePercentage(int openingGatePercentage) {
        this.openingGatePercentage = openingGatePercentage;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getSuggestedOpeningLevel")
    public String getSuggestedOpeningLevel() {
        return this.suggestedValveOpeningLevel;
    }

    public void setSuggestedOpeningLevel(final String suggestedLevel) {
        this.suggestedValveOpeningLevel = suggestedLevel;
    }
}

@Controller
class WebController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}