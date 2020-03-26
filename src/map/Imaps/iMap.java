package map.Imaps;

import javafx.scene.layout.Pane;
import map.maps.Street;

public interface iMap{

    void addStreet(Street street);
    java.util.List<Street> getStreets();
    void draw(Pane mapCanvas);

}