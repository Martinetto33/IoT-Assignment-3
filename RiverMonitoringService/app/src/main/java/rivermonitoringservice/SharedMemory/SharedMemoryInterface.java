package rivermonitoringservice.SharedMemory;

public interface SharedMemoryInterface {

    public double getWaterLevel();

    public void setWaterLevel(double waterLevel);

    public int getOpeningGatePercentage();

    public void setOpeningGatePercentage(int openingGatePercentage);

    public String getStatus();

    public void setStatus(String status);

    public String getSuggestedValveOpeningLevel();

    public void setSuggestedValveOpeningLevel(String suggestedValveOpeningLevel);
}
