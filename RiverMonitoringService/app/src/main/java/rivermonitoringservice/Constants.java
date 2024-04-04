package rivermonitoringservice;

public class Constants {
    /*water levels are expressed in cm from the sensors. */
    private double waterLevel1 = 60.0;
    private double waterLevel2 = 40.0;
    private double waterLevel3 = 25.0;
    private double waterLevel4 = 10.0;
    private double f1 = 10;
    private double f2 = f1*2;
    
    public double getWaterLevel1() {
        return waterLevel1;
    }
    public double getWaterLevel2() {
        return waterLevel2;
    }
    public double getWaterLevel3() {
        return waterLevel3;
    }
    public double getWaterLevel4() {
        return waterLevel4;
    }
    public double getF1() {
        return f1;
    }
    public double getF2() {
        return f2;
    }
}
