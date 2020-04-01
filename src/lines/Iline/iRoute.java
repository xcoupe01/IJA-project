package lines.Iline;

import javafx.scene.layout.Pane;
import map.maps.Coordinate;

public interface iRoute{
    boolean addStop(Coordinate c);
    boolean addPoint(Coordinate c);
    boolean completeRoute();
    java.util.List<Coordinate> getRoute();
    java.util.List<java.lang.String> getRouteType();
    void updateRouteHighlight(Pane mapCanvas);
}