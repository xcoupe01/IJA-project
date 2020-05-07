package map.maps;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import map.Imaps.iCoordinate;

import static java.lang.Math.sqrt;

/**
 * Coordinate class
 * implements interface iCoordinate
 * Used to represent position on map
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Coordinate implements iCoordinate{

    /** The X part of the Coordinate*/
    private int coordX;
    /** The Y part of the Coordinate*/
    private int coordY;
    /** Tells if the Coordinate is visible*/
    private boolean drawn;
    /** The graphical object of Coordinate*/
    private Circle coordinate = new Circle(3);
    /** Tells if the point was already moved*/
    private boolean moved;

    /**
     * Native constructor of Coordinate class
     * @param coordX is the value of coordinates X axis value
     * @param coordY is the value of coordinates Y axis value
     */
    public Coordinate(int coordX, int coordY){
        this.coordX = coordX;
        this.coordY = coordY;
        this.drawn = false;
    }

    /**
     * Creates Coordinate, only positive numbers allowed
     * (not used as often as the native constructor)
     * @param coordX is the value of coordinates X axis value
     * @param coordY is the value of coordinates Y axis value
     * @return the new coordinate
     */
    public static Coordinate create(int coordX, int coordY){
        if(coordX >= 0 && coordY >= 0){
            return new Coordinate(coordX, coordY);
        }
        return null;
    }

    /**
     * Tells the X part of Coordinate
     * @return coordinate X value
     */
    public int getX(){ return this.coordX; }

    /**
     * Returns the Y part of Coordinate
     * @return coordinate Y value
     */
    public int getY(){ return this.coordY; }

    /**
     * Returns the difference on X (distance) axis between two points
     * @param c is coordinate to measure the difference to
     * @return X distance to coordinate "c"
     */
    public int diffX(Coordinate c){ return this.coordX - c.getX();}

    /**
     * Returns the difference on Y (distance) axis between two points
     * @param c is coordinate to measure the difference to
     * @return Y distance to coordinate "c"
     */
    public int diffY(Coordinate c){ return this.coordY - c.getY();}

    /**
     * Sets X part of coordinate to given number
     * @param coordX is the number X is going to be set to
     */
    public void setX(int coordX){ this.coordX = coordX; }

    /**
     * Sets Y part of coordinate to given number
     * @param coordY is the number Y is going to be set to
     */
    public void setY(int coordY){ this.coordY = coordY; }

    /**
     * Moves coordinate with given numbers
     * @param mapCanvas is the Pane on which is coordinate drawn
     * @param x is the difference in X axis we need to add to move the coord correctly
     * @param y is the difference in Y axis we need to add to move the coord correctly
     */
    public void moveCoord(Pane mapCanvas, int x, int y){
        if(!this.moved){
            if(this.drawn){
                this.erase(mapCanvas);
            }
            this.coordX += x;
            this.coordY += y;
            if(this.drawn){
                this.draw(mapCanvas);
            }
            this.moved = true;
        }
    }

    /**
     * Makes the Coordinate visible on given Pane
     * @param mapCanvas is the Pane where the coord is going to be drawn
     */
    public void draw(Pane mapCanvas){
        if(!drawn) {
            this.coordinate.setStroke(Color.BLACK);
            this.coordinate.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
            this.coordinate.relocate(coordX - 3, coordY - 3);
            this.coordinate.setOnMouseClicked(t -> System.out.println("Im coordinate at X:" + coordX + " Y:" + coordY));
            mapCanvas.getChildren().add(this.coordinate);
            this.drawn = true;
        }
    }

    /**
     * Makes the Coordinate disappear on given Pane
     * @param mapCanvas is the Pane where the coord is going to be erased
     */
    public void erase(Pane mapCanvas){
        mapCanvas.getChildren().remove(this.coordinate);
        this.drawn = false;
    }

    /**
     * Prepares Coordinate for moving. Its there to prevent multiple movements
     * when its unwanted. Use this to let the coordinate move, after moving you need
     * call this function again.
     */
    public void setMoved(){ this.moved = false; }

    /**
     * Tell if the point was moved
     * @return true if the coordinate is not prepared for movement
     */
    public boolean getMoved(){ return this.moved; }

    /**
     * Returns distance between this coordinate and coordinate "c"
     * @param c is the coordinate to measure the distance to
     * @return the distance of the coordinates
     */
    public double distance(Coordinate c){
        int distX = this.coordX - c.getX();
        int distY = this.coordY - c.getY();
        return sqrt( distX*distX + distY*distY );
    }

    @Override
    public boolean equals(java.lang.Object obj){
        if(obj instanceof Coordinate){
            return (this.coordX == ((Coordinate) obj).getX() && this.coordY == ((Coordinate) obj).getY());
        }
        return false;
    }

}