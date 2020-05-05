package map.Imaps;

import javafx.scene.layout.Pane;
import lines.line.PublicTransport;
import map.maps.Coordinate;
import map.maps.Stop;
import map.maps.Street;

public interface iMap{

    void addStreet(Street street);
    java.util.List<Street> getStreets();
    void draw(Pane mapCanvas);
    void moveMap(Pane mapCanvas, int x, int y);
    int getMapPointerById(java.lang.String id);
    boolean isStreet(java.lang.String id);
    void highlightOffAll(Pane mapCanvas);
    boolean loadMapFromFile(java.lang.String filePath, Pane mapCanvas);
    void saveMapToFile(java.lang.String filePath);
    Coordinate getCoord(Coordinate c);
    Stop getStopByCoord(Coordinate c);
    void attachPubTrans(PublicTransport mainPubTrans);
    void attachInformationPane(Pane informationPane);
}