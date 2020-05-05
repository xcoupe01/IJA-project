package map.maps;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import lines.line.PublicTransport;
import map.Imaps.iStop;
import static javafx.scene.Cursor.cursor;

public class Stop implements iStop{

    private Coordinate coord;                              //< Coordinates of the stop
    private java.lang.String name;                         //< name of the stop
    private Street street;                                 //< Street to which this stop belongs
    private boolean drawn;                                 //< tells if the Stop is visible
    private Rectangle stop = new Rectangle(6, 6);   //< graphical stop object
    private PublicTransport mainPubTrans;
    private Pane informationPane;

    public Stop(java.lang.String name, Coordinate coord, PublicTransport mainPubTrans, Pane informationPane){
        this.name = name;
        this.coord = coord;
        this.drawn = false;
        this.stop.setStroke(Color.BLACK);
        this.stop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
        this.stop.relocate(coord.getX() - 3, coord.getY() - 3);
        this.stop.setCursor(cursor("HAND"));
        this.mainPubTrans = mainPubTrans;
        this.informationPane = informationPane;
        this.stop.setOnMouseClicked(t -> {
            System.out.println(this.name);
            /*TODO here will be stop information itinerary *mainPubTrans* and *informationPane* will be used*/
        });
    }

    // returns the Coordinates of the stop
    public Coordinate getCoord(){ return this.coord; }

    // returns the name of the stop
    public java.lang.String getName(){ return this.name; }

    // returns the street to which Stop belongs
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

    // erases stop from mapCanvas
    public void erase(Pane mapCanvas){
        if(this.drawn){
            mapCanvas.getChildren().remove(this.stop);
            this.drawn = false;
        }
    }

    // moves stop with specified value
    public void moveStop(Pane mapCanvas, int x, int y){
        this.coord.setMoved();
        this.coord.moveCoord(mapCanvas, x, y);
        if(this.drawn){
            this.erase(mapCanvas);
        }
        this.stop.relocate(coord.getX() - 3, coord.getY() - 3);
        if(this.drawn){
            this.draw(mapCanvas);
        }
    }

}