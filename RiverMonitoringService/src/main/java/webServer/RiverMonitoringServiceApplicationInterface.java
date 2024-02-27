package webServer;
/*
 *  the methods used to carry out communication between client and server through the use of post and get requests, 
 *  the first are used to update values in the server, while the second is used to obtain values from the server.
 *  Cross origin it used for accept any request from anyone
 *  Post/get mapping is used to create a post/get request on the specified url
 */
public interface RiverMonitoringServiceApplicationInterface {
    //POST to send data from web client to the server
    int dashboard(int gateOpening);
    //GET to obtain water level from server
    int getLevel();
    //GET to obtain status from server
    String getStatus();
    //GET to obtain the opening percentage of gate from server
    int getOpening();
}