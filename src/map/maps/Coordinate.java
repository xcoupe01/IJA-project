package map.maps;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import map.Imaps.iCoordinate;

import static java.lang.Math.sqrt;


public class Coordinate implements iCoordinate{

    private int coordX;                             //< the X part of the Coordinate
    private int coordY;                             //< the Y part of the Coordinate
    private boolean drawn;                          //< tells if the Coordinate is visible
    private Circle coordinate = new Circle(3);   //< the graphical object of Coordinate
    private boolean moved;                          //< tells if the point was already moved

    Coordinate(int coordX, int coordY){
        this.coordX = coordX;
        this.coordY = coordY;
        this.drawn = false;
    }

    // creates Coordinate, only positive numbers allowed
    public static Coordinate create(int coordX, int coordY){
        if(coordX >= 0 && coordY >= 0){
            return new Coordinate(coordX, coordY);
        }
        return null;
    }

    // returns the X part of Coordinate
    public int getX(){ return this.coordX; }

    // returns the Y part of Coordinate
    public int getY(){ return this.coordY; }

    // returns the difference on X axis between two points
    public int diffX(Coordinate c){ return this.coordX - c.getX();}

    // returns the difference on Y axis between two points
    public int diffY(Coordinate c){ return this.coordY - c.getY();}

    // sets X part of coordinate to given number
    public void setX(int coordX){ this.coordX = coordX; }

    // sets Y part of coordinate to given number
    public void setY(int coordY){ this.coordY = coordY; }

    // moves coordinate with given numbers
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

    // makes the Coordinate visible on given Pane
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

    // makes the Coordinate disappear on given Pane
    public void erase(Pane mapCanvas){
        mapCanvas.getChildren().remove(this.coordinate);
        this.drawn = false;
    }

    // prepares point for moving
    public void setMoved(){ this.moved = false; }

    // tell if the point was moved
    public boolean getMoved(){ return this.moved; }

    // returns distance between this coordinate and c
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