package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.Iline.iPTLine;
import map.maps.Coordinate;
import map.maps.Map;

public class PTLine implements iPTLine {
    private Route lineRoute;
    private java.util.List<Vehicle> lineVehicles = new java.util.ArrayList<>();
    private Color lineColor;
    private int lineNumber;
    private boolean drawn = false;

    public PTLine(int lineNumber,Color myColor, Map map){
        this.lineColor = myColor;
        this.lineNumber = lineNumber;
        this.lineRoute = new Route(map, this);
    }

    public void setRoute(Route toSet){ this.lineRoute = toSet;}

    public Route getRoute(){ return this.lineRoute;}

    public Color getLineColor(){ return this.lineColor; }

    public int getLineNumber(){ return this.lineNumber; }

    public void addVehicle(){
        Vehicle newVehicle = new Vehicle(this, this.lineRoute.getRoute().get(0));
        this.lineVehicles.add(newVehicle);
    }

    public void rideVehicles(Pane mapCanvas){
        for(int i = 0; i < this.lineVehicles.size(); i++){
            if(this.lineVehicles.get(i).reachedTarget() && this.lineVehicles.get(i).getWait() == 0){
                this.lineVehicles.get(i).setWait(5);
                int vehiclePointer = 0;
                Coordinate target;
                for(int j = 0; j < this.lineRoute.getRoute().size(); j++){
                    if(this.lineVehicles.get(i).getPosition().equals(this.lineRoute.getRoute().get(j))){
                        vehiclePointer = j;
                        break;
                    }
                }
                if(vehiclePointer == 0 || vehiclePointer == this.lineRoute.getRoute().size() - 1){
                    this.lineVehicles.get(i).switchOrientation();
                }
                if(this.lineVehicles.get(i).getOrientation()){
                    target = this.lineRoute.getRoute().get(vehiclePointer + 1);
                } else {
                    target = this.lineRoute.getRoute().get(vehiclePointer - 1);
                }
                this.lineVehicles.get(i).setTarget(target);
            } else if(this.lineVehicles.get(i).reachedTarget() && this.lineVehicles.get(i).getWait() == 1){
                this.lineVehicles.get(i).ride(mapCanvas);
                this.lineVehicles.get(i).ride(mapCanvas);
            } else {
                this.lineVehicles.get(i).ride(mapCanvas);
            }
        }
    }

    public void drawVehicles(Pane mapCanvas){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.draw(mapCanvas);
        }
    }

    public void eraseVehicles(Pane mapCanvas){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.draw(mapCanvas);
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
}