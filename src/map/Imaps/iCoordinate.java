package map.Imaps;


import javafx.scene.layout.Pane;
import map.maps.Coordinate;

public interface iCoordinate{

    int getX();
    int getY();
    int diffX(Coordinate c);
    int diffY(Coordinate c);
    void draw(Pane mapCanvas);
    void erase(Pane mapCanvas);
}