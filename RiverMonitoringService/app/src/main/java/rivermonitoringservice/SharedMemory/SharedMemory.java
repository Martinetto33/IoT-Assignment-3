package rivermonitoringservice.SharedMemory;

public class SharedMemory implements SharedMemoryInterface{
    private double waterLevel = 0;		        //indicates the height of the water
	private int openingGatePercentage = 0;	//indicates the opening of the valve %
	private String status = "a";			//indicates the status of the system
    private String suggestedValveOpeningLevel = "";
    
    public double getWaterLevel() {
        return waterLevel;
    }
    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }
    public int getOpeningGatePercentage() {
        return openingGatePercentage;
    }
    public void setOpeningGatePercentage(int openingGatePercentage) {
        this.openingGatePercentage = openingGatePercentage;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSuggestedValveOpeningLevel() {
        return suggestedValveOpeningLevel;
    }
    public void setSuggestedValveOpeningLevel(String suggestedValveOpeningLevel) {
        this.suggestedValveOpeningLevel = suggestedValveOpeningLevel;
    }

}
