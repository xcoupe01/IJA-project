package map.Imaps;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;
import map.maps.Stop;
import map.maps.Street;

/**
 * iStreet - interface of Street class
 * implemented by class Street
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iStreet{

    /**
     * Add stop to the street
     * @param stop is the stop to be added to the street
     * @return true if successful false otherwise
     */
    boolean addStop(Stop stop);

    /**
     * Returns the route in coordinates of the street
     * @return list of coordinates which defines the street from start to end with points and stops
     */
    java.util.List<Coordinate> getStreetRoute();

    /**
     * Returns the route in type of the street
     * @return list of types that corresponds to coordinates form "streetRoute"
     */
    java.util.List<java.lang.String> getStreetRouteType();

    /**
     * Tells if coordinate is on the street but its based on the position of the given stop
     * not if the stop is assigned to this street.
     * @param stop is the stop to be tested
     * @return true if the stop is on the street
     */
    boolean isStopOnStreet(Stop stop);

    /**
     * Adds new coordinate to define the street
     * @param coord is the coordinate that's going to be added
     */
    void addCoord(Coordinate coord);

    /**
     * Returns street id (name probably)
     * @return street id
     */
    java.lang.String getId();

    /**
     * Returns list of Coordinates which defines the street
     * @return list of defining coordinates
     */
    java.util.List<Coordinate> getCoordinates();

    /**
     * Tells the street begin
     * @return coordinate of the first defining point
     */
    Coordinate begin();

    /**
     * Tells the street end
     * @return coordinate of the last defining point
     */
    Coordinate end();

    /**
     * Returns list of stops that belongs to the street
     * @return list of stops attached to the street
     */
    java.util.List<Stop> getStops();

    /**
     * Checks if two streets follow each other
     * @param s is the street to be checked
     * @return true if any defining point of the given street
     * is the same as any defining point of this street
     */
    boolean follows(Street s);

    /**
     * Draws the street and its all stops to given Pane
     * @param mapCanvas is the Pane where the street is going to be drawn
     */
    void draw(Pane mapCanvas);

    /**
     * Erases the street with its all stops from given Pane
     * @param mapCanvas is the Pane where the street will disappear
     */
    void erase(Pane mapCanvas);

    /**
     * Returns true if the street is visible (probably on mapCanvas)
     * @return true if the street is visible
     */
    boolean getDrawn();

    /**
     * Returns true if the street highlight is visible (probably on mapCanvas)
     * @return true if the street highlight is visible
     */
    boolean getHighlightStatus();

    /**
     * Makes highlight visible on mapCanvas
     * @param mapCanvas is the Pane where the highlight is going to be drawn
     */
    void highlightOn(Pane mapCanvas);

    /**
     * Makes highlight disappear on mapCanvas
     * @param mapCanvas is the Pane where the highlight is going to be erased
     */
    void highlightOff(Pane mapCanvas);

    /**
     * Toggles highlight on mapCanvas (if its on it turns it of ect.)
     * @param mapCanvas is the Pane where the highlight will be toggled
     */
    void highlightToggle(Pane mapCanvas);

    /**
     * Moves whole street with specified values
     * @param mapCanvas is the Pane where the street is going to be moved
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    void moveStreet(Pane mapCanvas, int x, int y);

    /**
     * Tell shortest point on street to mouse cursor *-- my loved masterpiece*
     * @param mouse is the position of the mouse in coordinate structure (or any other position)
     * @return the closest coordinate to given coordinate "mouse" that is on the street
     */
    Coordinate shortestPointToCoord(Coordinate mouse);

    /**
     * Removes last coord of street (must remain at least 2 coords) with all stops on this segment
     * @param mapCanvas is the Pane where the street is drawn
     */
    void removeLastCoord(Pane mapCanvas);

    /**
     * Removes stop at given coordinates from the street
     * @param c is the coordinate of stop that's going to be removed
     * @param mapCanvas is the Pane where the street is drawn
     */
    void removeStop(Coordinate c, Pane mapCanvas);

    /**
     * Returns traffic value between two points on the street, it don't depends on the order of the given coordinates
     * @param c1 one of the coordinates
     * @param c2 one of the coordinates
     * @return the value of traffic, if it returns -1 it means error
     */
    int getTrafficAt(Coordinate c1, Coordinate c2);

    /**
     * Sets traffic on a given index. If index is greater than the field size or below 0
     * nothing is going to happen, if the traffic value is below 1, traffic 1 is going to be set,
     * if traffic value is above 5, traffic 5 is going to be set. Only traffic values 1 to 5 are allowed.
     * @param index is the traffic segment index we want to set
     * @param traffic is the traffic value we want to set
     */
    void setTraffic(int index, int traffic);

    /**
     * Returns array of integers representing traffic levels on segments of the street
     * @return array of integers representing traffic
     */
    java.util.List<Integer> getTraffic();

    /**
     * Toggles a given street segment highlight (when whole street is highlighted, it erases highlight and
     * highlights only the specified part, when street highlight is off it highlights the specified segment)
     * @param index specifies the street segment
     * @param mapCanvas is the Pane where we want the highlight visible
     */
    void toggleSegmentHighlight(int index, Pane mapCanvas);
}