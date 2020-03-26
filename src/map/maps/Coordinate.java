package map.maps;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import map.Imaps.iCoordinate;


public class Coordinate implements iCoordinate{

    private int coordX;                             //< the X part of the Coordinate
    private int coordY;                             //< the Y part of the Coordinate
    private boolean drawn;                          //< tells if the Coordinate is visible
    private Circle coordinate = new Circle(3);   //< the graphical object of Coordinate

    private Coordinate(int coordX, int coordY){
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

    @Override

    public boolean equals(java.lang.Object obj){
        if(obj instanceof Coordinate){
            return (this.coordX == ((Coordinate) obj).getX() && this.coordY == ((Coordinate) obj).getY());
        }
        return false;
    }

}