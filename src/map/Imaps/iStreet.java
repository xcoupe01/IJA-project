package map.Imaps;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;
import map.maps.Stop;
import map.maps.Street;

public interface iStreet{

    void addStop(Stop stop);
    void addCoord(Coordinate c);
    java.lang.String getId();
    java.util.List<Coordinate> getCoordinates();
    Coordinate begin();
    Coordinate end();
    java.util.List<Stop> getStops();
    boolean follows(Street s);
    void draw(Pane mapCanvas);
    void erase(Pane mapCanvas);
    boolean getDrawn();
    boolean getHighlightStatus();
    void highlightOn(Pane mapCanvas);
    void highlightOff(Pane mapCanvas);
    void highlightToggle(Pane mapCanvas);

}