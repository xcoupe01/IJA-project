package map.Imaps;

import javafx.scene.layout.Pane;
import lines.line.PublicTransport;
import map.maps.Coordinate;
import map.maps.Stop;
import map.maps.Street;

/**
 * iMap - interface of Map class
 * implemented by class Map
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iMap{

    /**
     * Adds given street to map
     * @param street is street that's going to be added to map
     */
    void addStreet(Street street);

    /**
     * Returns all streets saved in map
     * @return list of streets
     */
    java.util.List<Street> getStreets();

    /**
     * Makes the whole map visible on given Pane
     * @param mapCanvas the Pane that the map is going to be drawn to
     */
    void draw(Pane mapCanvas);

    /**
     * Moves all objects in map
     * @param mapCanvas is Pane where the map is drawn
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    void moveMap(Pane mapCanvas, int x, int y);

    /**
     * Returns list index (pointer) into street list in this map for a given street
     * @param id is the id (probably name) of street
     * @return list index into street list to street that matches given id (name)
     */
    int getMapPointerById(java.lang.String id);

    /**
     * Tells if given id coresponds to some id (name) of some street in street list
     * @param id is the id (name) to be inspected
     * @return true if the id (name) is found false otherwise
     */
    boolean isStreet(java.lang.String id);

    /**
     * Turns all street highlights of on given Pane
     * @param mapCanvas is the Pane where the highlight is going to be turned off
     */
    void highlightOffAll(Pane mapCanvas);

    /**
     * Loads map from a specified file and draws it on given Pane
     * @param filePath is the path to the file (.map file recommended)
     * @param mapCanvas is the Pane where the map is going to be displayed
     * @return true if successful false otherwise
     */
    boolean loadMapFromFile(java.lang.String filePath, Pane mapCanvas);

    /**
     * Generates map save file to a given path
     * @param filePath is the file to which the map is going to be saved
     */
    void saveMapToFile(java.lang.String filePath);

    /**
     * Returns coordinate object from map with same position as given coordinate
     * @param c is the coordinate to be found
     * @return coordinate with same position from map
     */
    Coordinate getCoord(Coordinate c);

    /**
     * Returns stop object from map with the same position as given coordinate
     * @param c is the coordinate to be found
     * @return stop with the same position from map
     */
    Stop getStopByCoord(Coordinate c);

    /**
     * Attaches public transport connection to map
     * @param mainPubTrans is the public transport to be added
     */
    void attachPubTrans(PublicTransport mainPubTrans);

    /**
     * Attaches information Pane connection to map
     * @param informationPane is the Pane to be attached
     */
    void attachInformationPane(Pane informationPane);

    /**
     * Sets all stops occupation property to false
     */
    void setAllStopInformationPaneOccupyFalse();

    /**
     * Returns the stop that haves occupation property set to true
     * @return stop with occupation property equals to true
     */
    Stop getStopThatOccupiesInformationPane();
}