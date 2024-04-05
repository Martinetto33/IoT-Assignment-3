package rivermonitoringservice.data;

import java.util.Optional;

/**
 * Record classes are immutable and provide public getters for their fields.
 * For further details see:
 * https://www.baeldung.com/java-record-keyword
 * 
 * This class will model a simple type of data packet exchanged among the
 * subsystems that compose the River Monitoring Service.
 */
public record RiverMonitoringServiceData(double waterLevel,
                                         int valveOpeningPercentage,
                                         Optional<Integer> frontendRequiredOpeningPercentage) {}
