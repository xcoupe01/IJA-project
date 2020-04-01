package lines.line;

import javafx.scene.paint.Color;
import lines.Iline.iPTLine;

public class PTLine implements iPTLine {
    private Route lineRoute = null;
    private java.util.List<Vehicle> lineVehicles = new java.util.ArrayList<>();
    private Color lineColor;
    private int lineNumber;

    public PTLine(int lineNumber,Color myColor){
        this.lineColor = myColor;
        this.lineNumber = lineNumber;
    }

    public Color getLineColor(){ return this.lineColor; }

    public int getLineNumber(){ return this.lineNumber; }

    public void addVehicle(){
        Vehicle newVehicle = new Vehicle(this, this.lineRoute.getRoute().get(0));
        this.lineVehicles.add(newVehicle);
    }
}