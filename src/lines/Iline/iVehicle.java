package lines.Iline;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;

public interface iVehicle{
    boolean reachedTarget();
    void setTarget(Coordinate target);
    void ride(Pane mapCanvas);
    void draw(Pane mapCanvas);
    void erase(Pane mapCanvas);
    void setWait(int wait);
    int getWait();
    void switchOrientation();
    boolean getOrientation();
}