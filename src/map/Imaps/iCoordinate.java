package map.Imaps;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;

/**
 * iCoordinate - interface of Coordinate class
 * implemented by class Coordinate
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public interface iCoordinate{

    /**
     * tells the X part of Coordinate
     * @return int coordinate X value
     */
    int getX();

    /**
     * returns the Y part of Coordinate
     * @return int coordinate Y value
     */
    int getY();

    /**
     * returns the difference on X (distance) axis between two points
     * @param c is coordinate to measure the difference to
     * @return int X distance to coordinate "c"
     */
    int diffX(Coordinate c);

    /**
     * returns the difference on Y (distance) axis between two points
     * @param c is coordinate to measure the difference to
     * @return int Y distance to coordinate "c"
     */
    int diffY(Coordinate c);

    /**
     * sets X part of coordinate to given number
     * @param coordX is the number X is going to be set to
     */
    void setX(int coordX);

    /**
     * sets Y part of coordinate to given number
     * @param coordY is the number Y is going to be set to
     */
    void setY(int coordY);

    /**
     * moves coordinate with given numbers
     * @param mapCanvas is the Pane on which is coordinate drawn
     * @param x is the difference in X axis we need to add to move the coord correctly
     * @param y is the difference in Y axis we need to add to move the coord correctly
     */
    void moveCoord(Pane mapCanvas, int x, int y);

    /**
     * makes the Coordinate visible on given Pane
     * @param mapCanvas is the Pane where the coord is going to be drawn
     */
    void draw(Pane mapCanvas);

    /**
     * makes the Coordinate disappear on given Pane
     * @param mapCanvas is the Pane where the coord is going to be erased
     */
    void erase(Pane mapCanvas);

    /**
     * prepares Coordinate for moving. Its there to prevent multiple movements
     * when its unwanted. Use this to let the coordinate move, after moving you need
     * call this function again.
     */
    void setMoved();

    /**
     * tell if the point was moved
     * @return boolean true if the coordinate is not prepared for movement
     */
    boolean getMoved();

    /**
     * returns distance between this coordinate and coordinate "c"
     * @param c is the coordinate to measure the distance to
     * @return double the distance of the coordinates
     */
    double distance(Coordinate c);
}