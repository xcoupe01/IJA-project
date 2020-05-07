package lines.Iline;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;

/**
 * iRoute interface
 * implemented by class Route
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iRoute{

    /**
     * Tells if a given coordinate can be added to route. The condition is that the point must be in the map,
     * the first point must be a stop and the the next must be directly connected with street.
     * @param c is the point to be checked
     * @param type type of the point, "point" if regular coordinate "stop" if stop
     * @return true if it matches the condition false otherwise
     */
    boolean canAdd(Coordinate c, java.lang.String type);

    /**
     * Adds given stop to route if its possible
     * @param c is the coordinate of the stop we want to add
     * @return true if the operation was successful false otherwise
     */
    boolean addStop(Coordinate c);

    /**
     * Adds given point to route if its possible
     * @param c is the coordinate we want to add
     * @return true if the operation was successful
     */
    boolean addPoint(Coordinate c);

    /**
     * Updates route highlight on given Pane
     * @param mapCanvas is the Pane where the highlight is going to be updated
     */
    void updateRouteHighlight(Pane mapCanvas);

    /**
     * Tells if the route is complete which means if the first and last points are stops
     * @return true if the route is complete
     */
    boolean completeRoute();

    /**
     * Returns the list of coordinates that describes the route
     * @return list of coordinates that describes the route
     */
    java.util.List<Coordinate> getRoute();

    /**
     * Returns the list of coordinate types that describes the route
     * @return list of coordinate types that describes the route
     */
    java.util.List<java.lang.String> getRouteType();

    /**
     * Erase the route highlight from a given Pane
     * @param mapCanvas the Pane where the highlight route is going to be erased
     */
    void erase(Pane mapCanvas);

    /**
     * Removes last point that defines route
     * @param mapCanvas is the Pane where the route is drawn
     */
    void removeLast(Pane mapCanvas);
}