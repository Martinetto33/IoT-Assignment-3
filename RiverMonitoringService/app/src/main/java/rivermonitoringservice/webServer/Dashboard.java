package rivermonitoringservice.webServer;

import java.util.Optional;

import rivermonitoringservice.SharedMemory.SharedMemory;

/*
 *  the methods used to carry out communication between client and server through the use of post and get requests, 
 *  the first are used to update values in the server, while the second is used to obtain values from the server.
 *  Cross origin it used for accept any request from anyone
 *  Post/get mapping is used to create a post/get request on the specified url
 */
public interface Dashboard {
    
    /**
     * Retrieves all relevant information to update the Dashboard from the provided instance of
     * shared memory.
     * @param shMemory the shared memory containing the actual data measured by the physical sensors.
     */
    void refreshDashboardWithDataFromSharedMemory(final SharedMemory shMemory);

    /**
     * A method to check if a web application user requested a change of the opening level of the valve.
     * @return true if a user requested a change, false otherwise.
     */
    boolean wasNewOpeningLevelRequested();

    /**
     * A method to notify the dashboard that the request was handled. This
     * doesn't necessarily imply that the valve is now opened at the requested level,
     * since the Water Channel Controller may be in manual mode.
     */
    void notifyThatRequestWasHandled();

    /**
     * Returns an Optional containing the requested opening level,
     * if present.
     * @return an Optional with the requested opening level percentage if present,
     * or an empty Optional if no new request arrived from the dashboard.
     */
    Optional<Integer> getUserRequestedOpeningLevel();
}