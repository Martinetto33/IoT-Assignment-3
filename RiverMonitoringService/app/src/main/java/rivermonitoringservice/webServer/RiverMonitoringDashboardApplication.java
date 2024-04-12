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

import rivermonitoringservice.SharedMemory.SharedMemory;

@SpringBootApplication
@RestController
public class RiverMonitoringDashboardApplication implements RiverMonitoringDashboardApplicationInterface {
	private SharedMemory shMemory; 
    
    public void startWebServer(String[] args, SharedMemory sharedMemory) {
        SpringApplication.run(RiverMonitoringDashboardApplication.class, args);
        this.shMemory = sharedMemory;
    }
    
    @Override
    @CrossOrigin(origins = "*")
    @PostMapping("/dashboard")
    public int dashboard(@RequestParam(value = "level")int gateOpening) {
        try {
            this.shMemory.setOpeningGatePercentage(gateOpening);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.shMemory.getOpeningGatePercentage();
    }
    
    @Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getLevel")
    public double getLevel() {
        return this.shMemory.getWaterLevel();
    }
    
	@Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getStatus")
    public String getStatus() {
        return this.shMemory.getStatus();
    }
    
	@Override
    @CrossOrigin(origins = "*")
    @GetMapping("/getGatePercentage")
    public int getOpening() {
        return this.shMemory.getOpeningGatePercentage();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getSuggestedOpeningLevel")
    public String getSuggestedOpeningLevel() {
        return this.shMemory.getSuggestedValveOpeningLevel();
    }
}

@Controller
class WebController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}