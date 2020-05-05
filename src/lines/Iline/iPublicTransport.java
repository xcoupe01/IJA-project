package lines.Iline;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lines.line.PTLine;
import lines.line.Timer;
import map.maps.Map;


public interface iPublicTransport{
    void addLine(PTLine line);
    java.util.List<PTLine> getLines();
    void updatePTPos(Pane mapCanvas, int x, int y);
    boolean loadPTFromFile(Pane mapCanvas, String filePath, Map mainMap, Pane lineInformationPane);
    void savePTToFile(String filePath, Map mainMap);
    void eraseAllVehicles(Pane mapCanvas);
    void drawAllVehicles(Pane mapCanvas);
    void highlightAllRoutesOn(Pane mapCanvas);
    void highlightAllRoutesOff(Pane mapCanvas);
    void toggleHighlight(Pane mapCanvas);
    void rideAllVehicles(Pane mapCanvas);
    void setTimeDisplay(Label timeDisplay);
    Timer getTimer();
    void updateTimeDisplay();
}