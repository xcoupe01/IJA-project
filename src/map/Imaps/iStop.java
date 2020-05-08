package map.Imaps;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;
import map.maps.Street;

/**
 * iStreet - interface of Street class
 * implemented by class Street
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iStop{

    /**
     * Returns the Coordinates of the stop
     * @return coordinate of the stop
     */
    Coordinate getCoord();

    /**
     * Returns the name of the stop
     * @return name of the stop
     */
    java.lang.String getName();

    /**
     * Returns the street to which Stop belongs
     * @return street to which this stop belongs
     */
    Street getStreet();

    /**
     * Sets the street to which stop belongs
     * @param street is the street to which the stop is going to be signed
     */
    void setStreet(Street street);

    /**
     * Makes the stop visible on given Pane
     * @param mapCanvas is the Pane where the stop is going to be drawn
     */
    void draw(Pane mapCanvas);

    /**
     * Erases stop from a given Pane
     * @param mapCanvas is the pane where the stop is going to be erased
     */
    void erase(Pane mapCanvas);

    /**
     * Moves stop with specified value
     * @param mapCanvas is the Pane where the stop is drawn
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    void moveStop(Pane mapCanvas, int x, int y);

    /**
     * Draws information about stop such as stop name, lines that stops at this
     * stop and time that vehicles of some line need to get to this stop
     */
    void drawInformation();

    /**
     * Returns if the information in the information panel is being displayed
     * @return true if stop is displaying its information
     */
    boolean getInformationPaneOccupy();

    /**
     * Sets information pane occupation to false
     */
    void InformationPaneOccupyFalse();

    /**
     * Prepares the stop for removal
     */
    void removeProcedure();
}