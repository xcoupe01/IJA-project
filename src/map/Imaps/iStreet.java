package map.Imaps;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;
import map.maps.Stop;
import map.maps.Street;

public interface iStreet{

    boolean addStop(Stop stop);
    boolean isStopOnStreet(Stop stop);
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
    void moveStreet(Pane mapCanvas, int x, int y);
    Coordinate shortestPointToCoord(Coordinate mouse);
    void removeLastCoord(Pane mapCanvas);
    java.util.List<Coordinate> getStreetRoute();
    java.util.List<java.lang.String> getStreetRouteType();
    void removeStop(Coordinate c, Pane mapCanvas);

}