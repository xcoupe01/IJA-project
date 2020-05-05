package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.Iline.iPTLine;
import map.maps.Map;

public class PTLine implements iPTLine {
    private Route lineRoute;
    private java.util.List<Vehicle> lineVehicles = new java.util.ArrayList<>();
    private Color lineColor;
    private int lineNumber;
    private boolean drawn = false;
    private Map mainMap;

    public PTLine(int lineNumber,Color myColor, Map map){
        this.lineColor = myColor;
        this.lineNumber = lineNumber;
        this.lineRoute = new Route(map, this);
        this.mainMap = map;
    }

    public void setRoute(Route toSet){ this.lineRoute = toSet;}

    public Route getRoute(){ return this.lineRoute;}

    public Color getLineColor(){ return this.lineColor; }

    public int getLineNumber(){ return this.lineNumber; }

    public void addVehicle(int linePoint, PublicTransport mainPubTrans){
        Vehicle newVehicle = new Vehicle(this, this.lineRoute.getRoute().get(linePoint), this.lineVehicles.size() + 1, mainMap, mainPubTrans);
        this.lineVehicles.add(newVehicle);
    }

    public void drawVehicles(Pane mapCanvas){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.draw(mapCanvas);
        }
    }

    public void eraseVehicles(Pane mapCanvas){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.erase(mapCanvas);
        }
    }

    public void drawLineHighlight(Pane mapCanvas){
        if(!this.drawn){
            this.drawn = true;
            this.lineRoute.draw(mapCanvas);
        }
    }

    public void eraseLineHighlight(Pane mapCanvas){
        if(drawn){
            this.drawn = false;
            this.lineRoute.erase(mapCanvas);
        }
    }

    public void toggleLineHighlight(Pane mapCanvas) {
        if(this.drawn){
            this.eraseLineHighlight(mapCanvas);
        } else {
            this.drawLineHighlight(mapCanvas);
        }
    }

    public void rideAllVehicles(){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.ride();
        }
    }

    public void updateVehicles(int x, int y){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.move(x, y);
        }
    }

    public void setVehiclesTickMeansSec(int num){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.setTurnMeansSec(num);
        }
    }

    public void setVehiclesTicksAtStop(int num){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.setTurnsAtStop(num);
        }
    }

    java.util.List<Vehicle> getVehicles(){ return this.lineVehicles;}
}