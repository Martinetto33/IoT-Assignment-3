package rivermonitoringservice.webServer;

import org.springframework.stereotype.Component;

@Component
public class DashboardClone implements RiverMonitoringDashboardApplicationInterface {
    private double waterLevel = 0.0;		        //indicates the height of the water
	private int openingGatePercentage = 0;	//indicates the opening of the valve %
	private String status = "a";			//indicates the status of the system
    private String suggestedValveOpeningLevel = "";

    public DashboardClone() {}

    @Override
    public int dashboard(int gateOpening) {
        this.openingGatePercentage = gateOpening;
        return this.openingGatePercentage;
    }

    @Override
    public double getLevel() {
        return this.waterLevel;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public int getOpening() {
        return this.openingGatePercentage;
    }

    @Override
    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    @Override
    public void setSuggestedOpeningLevel(String suggestedLevel) {
        this.suggestedValveOpeningLevel = suggestedLevel;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuggestedOpeningLevel() {
        return this.suggestedValveOpeningLevel;
    }

    public void setGateOpeningLevel(int openingLevel) {
        this.openingGatePercentage = openingLevel;
    }

    @Override
    public void startWebServer(String[] args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startWebServer'");
    }

}
