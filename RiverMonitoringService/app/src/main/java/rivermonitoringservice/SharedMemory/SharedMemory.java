package rivermonitoringservice.SharedMemory;

/**
 * A class containing the values measured by the sensors of the subsystems, used
 * to simplify
 * communication and encapsulate fields.
 */
public class SharedMemory implements SharedMemoryInterface {
    private double waterLevel = 0; // indicates the height of the water
    private int openingGatePercentage = 0; // indicates the opening of the valve %
    private String status = "a"; // indicates the status of the whole system
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
