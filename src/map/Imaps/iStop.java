package map.Imaps;


import javafx.scene.layout.Pane;
import map.maps.Coordinate;
import map.maps.Street;

public interface iStop{

    Coordinate getCoord();
    java.lang.String getName();
    Street getStreet();
    void setStreet(Street street);
    void draw(Pane mapCanvas);
    void erase(Pane mapCanvas);
    void moveStop(Pane mapCanvas, int x, int y);

}