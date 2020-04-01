package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lines.Iline.iVehicle;
import map.maps.Coordinate;

public class Vehicle implements iVehicle {
    private Coordinate coord;
    private Coordinate target;
    private Circle vehicle = new Circle(3);
    private PTLine line;
    private boolean drawn = false;

    public Vehicle(PTLine line, Coordinate start){
        this.coord = new Coordinate(start.getX(), start.getY());
        this.line = line;
        this.vehicle.setStroke(Color.BLACK);
        this.vehicle.relocate(this.coord.getX() - 1.5 , this.coord.getY() - 1.5 );
    }

    public boolean reachedTarget(){ return this.target.equals(this.coord);}

    public void setTarget(Coordinate target){ this.target = target;}

    public void ride(Pane mapCanvas){

    }

    public void draw(Pane mapCanvas){
        if(!this.drawn){
            this.vehicle.setFill(line.getLineColor());
            mapCanvas.getChildren().add(this.vehicle);
            this.drawn = true;
        }
    }

    public void erase(Pane mapCanvas){
        if(this.drawn){
            mapCanvas.getChildren().remove(this.vehicle);
            this.drawn = false;
        }
    }


}