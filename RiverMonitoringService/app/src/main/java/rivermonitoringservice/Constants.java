package rivermonitoringservice;

import com.google.common.collect.Range;

public interface Constants {
    /*water levels are expressed in cm from the sensors. */
    static double waterLevel1 = 60.0;
    static double waterLevel2 = 40.0;
    static double waterLevel3 = 25.0;
    static double waterLevel4 = 10.0;
    static int f1 = 10;
    static int f2 = f1 * 2;
    static int normalOpeningLv = 25;
    static int lowOpeningLv = 0;
    static int preTooHighOpeningLv = normalOpeningLv;
    static int tooHighOpeningLv = 50;
    static int tooHighCriticOpeningLv = 100;
    static Range<Double> normalRange = Range.closed(waterLevel1, waterLevel2); // [WL1, WL2]
    static Range<Double> lowRange = Range.greaterThan(waterLevel1); /* < W1; I know it may seem confusing, 
    but the constants specified here are the distances from the sensor, not the actual water level */
    static Range<Double> preHighRange = Range.openClosed(waterLevel2, waterLevel3); // (WL2, WL3]
    static Range<Double> highRange = Range.openClosed(waterLevel2, waterLevel3); // (WL3, WL4]
    static Range<Double> criticalRange = Range.lessThan(waterLevel4); // > WL4
}
