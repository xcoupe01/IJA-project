package lines.Iline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.line.PublicTransport;
import lines.line.Route;

public interface iPTLine{
    void setRoute(Route toSet);
    Route getRoute();
    int getLineNumber();
    Color getLineColor();
    void addVehicle(int linePoint, PublicTransport mainPubTrans);
    void drawVehicles(Pane mapCanvas);
    void drawLineHighlight(Pane mapCanvas);
    void eraseVehicles(Pane mapCanvas);
    void eraseLineHighlight(Pane mapCanvas);
    void toggleLineHighlight(Pane mapCanvas);
    void rideAllVehicles();
    void updateVehicles(int x, int y);

}