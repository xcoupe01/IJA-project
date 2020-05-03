package lines.Iline;

import javafx.scene.layout.Pane;
import lines.line.PTLine;
import map.maps.Map;

public interface iPublicTransport{
    void addLine(PTLine line);
    java.util.List<PTLine> getLines();
    void setTimer(int time);
    void updatePTPos(Pane mapCanvas, int x, int y);
    boolean loadPTFromFile(Pane mapCanvas, String filePath, Map mainMap);
    void savePTToFile(String filePath, Map mainMap);
    void eraseAllVehicles(Pane mapCanvas);
    void drawAllVehicles(Pane mapCanvas);
    void highlightAllRoutesOn(Pane mapCanvas);
    void highlightAllRoutesOff(Pane mapCanvas);
    void toggleHighlight(Pane mapCanvas);
    void rideAllVehicles(Pane mapCanvas);
}