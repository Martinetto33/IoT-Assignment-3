package rivermonitoringservice.webServer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class DashboardController {
    private final DashboardClone dashboard;

    public DashboardController(DashboardClone dashboard) {
        this.dashboard = dashboard;
    }

    @GetMapping("/dashboard") 
    public DashboardClone getDashboard() {
        return this.dashboard;
    }


    public DashboardClone getDashboardClone() {
        return this.dashboard;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/dashboard")
    public int dashboard(@RequestParam(value = "level")int gateOpening) {
        try {
			this.dashboard.setGateOpeningLevel(gateOpening);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.dashboard.getOpening();
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getLevel")
    public double getLevel() {
        return this.dashboard.getLevel();
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getStatus")
    public String getStatus() {
        return this.dashboard.getStatus();
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/getGatePercentage")
    public int getOpening() {
        return this.dashboard.getOpening();
    }

    public void setWaterLevel(double waterLevel) {
        this.dashboard.setWaterLevel(waterLevel);
    }
    
    public void setOpeningGatePercentage(int openingGatePercentage) {
        this.dashboard.setGateOpeningLevel(openingGatePercentage);
    }
    
    public void setStatus(String status) {
        this.dashboard.setStatus(status);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getSuggestedOpeningLevel")
    public String getSuggestedOpeningLevel() {
        return this.dashboard.getSuggestedOpeningLevel();
    }

    public void setSuggestedOpeningLevel(final String suggestedLevel) {
        this.dashboard.setSuggestedOpeningLevel(suggestedLevel);
    }
}

@Controller
class WebController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}