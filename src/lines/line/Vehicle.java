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
    private double travelPieceX;
    private double travelPieceY;
    private boolean drawn = false;
    private int wait;
    private boolean orientation = true;

    public Vehicle(PTLine line, Coordinate start){
        this.coord = new Coordinate(start.getX(), start.getY());
        this.line = line;
        this.vehicle.setStroke(Color.BLACK);
        this.vehicle.relocate(this.coord.getX() - 1.5 , this.coord.getY() - 1.5 );
    }

    public boolean reachedTarget(){ return this.target.equals(this.coord);}

    public void setTarget(Coordinate target){
        this.target = target;
        this.travelPieceX = this.coord.diffX(target) / this.coord.distance(this.target);
        this.travelPieceY = this.coord.diffY(target) / this.coord.distance(this.target);
    }

    public void ride(Pane mapCanvas){
        if(this.target != null && this.wait == 0){
            this.coord.setX(this.coord.getX() + (int) this.travelPieceX);
            this.coord.setY(this.coord.getY() + (int) this.travelPieceY);
            mapCanvas.getChildren().remove(this.vehicle);
            this.vehicle.relocate(this.coord.getX() - 1.5 , this.coord.getY() - 1.5 );
            if(drawn){
               mapCanvas.getChildren().add(this.vehicle);
            }
        } else if(this.wait != 0){
            this.wait --;
        }
    }

    public void draw(Pane mapCanvas){
        if(!this.drawn){
            this.vehicle.setFill(this.line.getLineColor());
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

    public void setWait(int wait){ this.wait = wait; }
    public int getWait(){return this.wait; }
    public Coordinate getPosition(){return this.coord; }
    public void switchOrientation(){ this.orientation = !this.orientation; }
    public boolean getOrientation(){ return this.orientation; }

}