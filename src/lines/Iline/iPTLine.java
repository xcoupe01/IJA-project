package lines.Iline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.line.Route;

public interface iPTLine{
    void setRoute(Route toSet);
    Route getRoute();
    int getLineNumber();
    Color getLineColor();
    void addVehicle();
    void drawVehicles(Pane mapCanvas);
    void drawLineHighlight(Pane mapCanvas);
    void eraseVehicles(Pane mapCanvas);
    void eraseLineHighlight(Pane mapCanvas);
    void rideVehicles(Pane mapCanvas);
    void toggleLineHighlight(Pane mapCanvas);

}