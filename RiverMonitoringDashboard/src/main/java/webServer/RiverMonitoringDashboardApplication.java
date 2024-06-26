package webServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication
@RestController
public class RiverMonitoringDashboardApplication implements RiverMonitoringDashboardApplicationInterface {
	private static double waterLevel = 0;		//indicates the height of the water
	private int openingGatePercentage = 0;	//indicates the opening of the valve %
	private String status = "a";			//indicates the status of the system
    private static Random random = new Random();
    public static void main(String[] args) {
      	SpringApplication.run(RiverMonitoringDashboardApplication.class, args);
        RiverMonitoringDashboardApplication.waterLevel = random.nextDouble(100.0); 
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
}

@Controller
class WebController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}