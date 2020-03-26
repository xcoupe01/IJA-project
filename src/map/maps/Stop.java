package map.maps;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import map.Imaps.iStop;
import static javafx.scene.Cursor.cursor;

public class Stop implements iStop{

    private Coordinate coord;                              //< Coordinates of the stop
    private java.lang.String name;                         //< name of the stop
    private Street street;                                 //< Street to which this stop belongs
    private boolean drawn;                                 //< tells if the Stop is visible
    private Rectangle stop = new Rectangle(5, 5);   //< graphical stop object

    public Stop(java.lang.String name, Coordinate coord){
        this.name = name;
        this.coord = coord;
        this.drawn = false;
        this.stop.setStroke(Color.BLACK);
        this.stop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
        this.stop.relocate(coord.getX() - 3, coord.getY() - 3);
        this.stop.setCursor(cursor("HAND"));
        this.stop.setOnMouseClicked(t -> System.out.println(name));
    }

    // returns the Coordinates of the stop
    public Coordinate getCoord(){ return this.coord; }

    // returns the name of the stop
    public java.lang.String getName(){ return this.name; }

    // retruns the street to which Stop belongs
    public Street getStreet(){ return this.street; }

    // sets the street to which Stop belongs
    public void setStreet(Street street){ this.street = street; }

    // makes the Stop visible on given Pane
    public void draw(Pane mapCanvas){
        if(!drawn) {
            mapCanvas.getChildren().add(this.stop);
            this.drawn = true;
        }
    }

    public void erase(Pane mapCanvas){
        mapCanvas.getChildren().remove(this.stop);
        this.drawn = false;
    }
    //TODO erase method

}