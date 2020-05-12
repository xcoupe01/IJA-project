package lines.Iline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.line.PTConnection;
import lines.line.PublicTransport;
import lines.line.Route;
import lines.line.Vehicle;
import map.maps.Coordinate;

/**
 * iPTLine interface
 * implemented by class PTLine
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iPTLine{

    /**
     * Adds route to the line
     * @param toSet is the route to be assigned to the route
     */
    void setRoute(Route toSet);

    /**
     * Returns the route of the line
     * @return the route of the line
     */
    Route getRoute();

    /**
     * Tells the line color
     * @return the line color
     */
    Color getLineColor();

    /**
     * Tells the line number
     * @return the line number
     */
    int getLineNumber();

    /**
     * Adds vehicle to the line, it makes sure that there are not two vehicles on the road with the same number
     * @param linePoint is where the vehicle is going to be added
     * @param mainPubTrans connection to public transport
     * @param mapCanvas is the map Pane where the vehicle will be visible
     */
    void addVehicle(int linePoint, PublicTransport mainPubTrans, Pane mapCanvas);

    /**
     * Draws all line vehicles
     */
    void drawVehicles();

    /**
     * Erases all line vehicles
     */
    void eraseVehicles();

    /**
     * Draws line route highlight on a given Pane
     * @param mapCanvas is the Pane where the line route highlight is going to be drawn
     */
    void drawLineHighlight(Pane mapCanvas);

    /**
     * Erases line route highlight on a given Pane
     * @param mapCanvas is the Pane where the line route is going to be erased
     */
    void eraseLineHighlight(Pane mapCanvas);

    /**
     * Toggles the line route highlight on a given Pane
     * @param mapCanvas is the Pane where the line route is going to be toggled
     */
    void toggleLineHighlight(Pane mapCanvas);

    /**
     * Makes one animation step with all vehicles
     */
    void rideAllVehicles();

    /**
     * Moves all objects of a line
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    void updateVehicles(int x, int y);

    /**
     * Sets all vehicles value that represents how many seconds means one animation step in application time
     * @param num the value that represents how many seconds means one animation step in application time
     */
    void setVehiclesTickMeansSec(int num);

    /**
     * Sets how many animation steps vehicle will wait at stop
     * @param num is the number of steps that vehicle waits at stop
     */
    void setVehiclesTicksAtStop(int num);

    /**
     * Tells list index of stop of the line if the given coordinates matches the position of the stop
     * @param stopCoord is the coordinate to be searched for
     * @return list index of a stop that matches the given coordinates -1 if any of the stops matches
     */
    int isStopInLineRoute(Coordinate stopCoord);

    /**
     * Returns the list of all line vehicles
     * @return the list of all line vehicles
     */
    java.util.List<Vehicle> getVehicles();

    /**
     * Removes the closest vehicle from the line to given point
     * @param mouseCoord is the coordinate that defines which vehicle is going to be erased
     * @param mapCanvas is the map Pane where the vehicle is going to be deleted (erased)
     */
    void removeNearestVehicle(Coordinate mouseCoord, Pane mapCanvas);

    /**
     * Returns the closest vehicle to a given point
     * @param mouseCoord is the coordinate that defines which vehicle is going to be returned
     * @return the closest vehicle to a given point
     */
    Vehicle getNearestVehicle(Coordinate mouseCoord);

    /**
     * Finds vehicle by vehicle number
     * @param vehicleNumber is the vehicle number to be searched for
     * @return the vehicle with given number or null if not found
     */
    Vehicle getVehicleByNumber(int vehicleNumber);

    /**
     * Removes vehicle with given vehicle number
     * @param vehicleNumber the vehicle number of vehicle to be removed
     */
    void removeVehicleByNumber(int vehicleNumber);

    /**
     * Adds a scheduled connection to a list of this connections of this line
     * @param toAdd connection to be added
     */
    void addScheduledConnection(PTConnection toAdd);

    /**
     * returns a list of scheduled connections on this line
     * @return list of connections on this line
     */
    java.util.List<PTConnection> getScheduledConnections();

    /**
     * checks if any of scheduled connection should depart or arrive (also works when the time is going backwards
     */
    void tickCheckScheduledConnections();

    /**
     * Refreshes all scheduled connections arrival times and active value
     */
    void refreshArrivalTimes();

    /**
     * Sets drawn value for all vehicles in the line
     * @param set the value that's set
     */
    void setVehiclesDrawn(boolean set);

    /**
     * Checks all line schedules for departures and arrivals
     */
    void removeScheduledConnection(int index);

    /**
     * Generates line time table based on scheduled lines in PNG format
     * @param filepath is the folder to be saved to
     * @param mainPubTrans is the connection for the public transport
     */
    void generateTimeScheduleToFile(String filepath, PublicTransport mainPubTrans);
}