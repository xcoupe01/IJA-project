package lines.Iline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
     * Adds vehicle to the line
     * @param linePoint is where the vehicle is going to be added
     * @param mainPubTrans connection to public transport
     */
    void addVehicle(int linePoint, PublicTransport mainPubTrans);

    /**
     * Draws all line vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be drawn
     */
    void drawVehicles(Pane mapCanvas);

    /**
     * Erases all line vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be erased
     */
    void eraseVehicles(Pane mapCanvas);

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
}