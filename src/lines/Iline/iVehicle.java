package lines.Iline;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;

/**
 * iVehicle interface
 * implemented by class Vehicle
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iVehicle{

    /**
     * Makes a step of vehicle
     */
    void ride();

    /**
     * Draws the vehicle to given Pane
     * @param mapCanvas is the Pane where the vehicle is going to be drawn
     */
    void draw(Pane mapCanvas);

    /**
     * Erases the vehicle from a given Pane
     * @param mapCanvas is the Pane where the vehicle is going to be erased
     */
    void erase(Pane mapCanvas);

    /**
     * Moves the vehicle with given values
     * @param x is the movement amount in X axis
     * @param y is the movement amount in Y axis
     */
    void move(int x, int y);

    /**
     * Sets the orientation of vehicle
     * @param setTo true means forward false means backward
     */
    void setForward(boolean setTo);


    /**
     * Tell the orientation of vehicle
     * @return true if forward false if backward
     */
    boolean getForward();

    /**
     * Returns the last point from the line route where the vehicle was
     * @return the list index of the last position of the line route where the vehicle was
     */
    int getStartPosition();

    /**
     * Tells how many steps the vehicle did from last route point
     * @return the amount of steps the vehicle did from last route point
     */
    int getTurns();

    /**
     * Attach the information Pane for the vehicle
     * @param informationPane the Pane to be attached
     */
    void setInformationPane(Pane informationPane);

    /**
     * Draws vehicle schedule on the information Pane
     */
    void drawInformation();

    /**
     * Tell how much time the vehicle needs to arrive to position in current ride. It also gives
     * negative values for stops that has already been visited this ride.
     * @param pos is the list index to line route coordinate list
     * @return amount in app seconds that the vehicle need to get to position (can be negative)
     */
    int howMuchTimeTo(int pos);

    /**
     * Tell how much time the vehicle needs to arrive to position. It returns only positive values
     * how many time it will take for the vehicle to get to stop.
     * @param pos is the list index to line route coordinate list
     * @return amount in app seconds that the vehicle need to get to position
     */
    int howMuchTimeToNext(int pos);

    /**
     * Sets amount of seconds that means one turn
     * @param num is the value that's being set
     */
    void setTurnMeansSec(int num);

    /**
     * Sets how many turns vehicle should wait at stop
     * @param num is the value how many turns the vehicle should wait
     */
    void setTurnsAtStop(int num);

    /**
     * Tells if this vehicle occupy the information Pane
     * @return true if vehicle occupies the information Pane false otherwise
     */
    boolean getInformationPaneOccupy();

    /**
     * Sets information Pane occupation value to false
     */
    void setInformationPaneOccupyFalse();

    /**
     * Returns the coordinate of the starting point of the vehicle
     * @return the coordinate of the starting point of the vehicle
     */
    Coordinate getStartCoordinate();

    /**
     * Switches the orientation of the vehicle anywhere on the route. It does not just
     * switch the "forward" value but rather prepares it for going other direction.
     */
    void switchDirection();

    /**
     * Returns current vehicle position
     * @return current vehicle position
     */
    Coordinate getPosition();

    /**
     * Prepares the vehicle for removal
     */
    void removeProcedure();
}