package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import lines.Iline.iVehicle;
import map.maps.Coordinate;

public class Vehicle implements iVehicle {
    private Circle vehicle = new Circle(3);
    private PTLine line;
    private Coordinate coord = new Coordinate(0,0);
    private Coordinate start;
    private Coordinate target = null;
    private String targetType;
    private double travelPieceX;
    private double travelPieceY;
    private double exactCoordX;
    private double exactCoordY;
    private boolean forward = true;
    private int wait = 0;
    private boolean drawn = false;

    public Vehicle(PTLine line, Coordinate start){
        this.line = line;
        this.vehicle.setStroke(Color.BLACK);
        this.vehicle.setFill(this.line.getLineColor());
        this.start = start;
        this.coord.setX(start.getX());
        this.coord.setY(start.getY());
        this.exactCoordX = start.getX();
        this.exactCoordY = start.getY();
        this.vehicle.relocate(this.coord.getX() - 3, this.coord.getY() - 3);
    }

    public void ride(){
        if(this.target == null){
            for(int i = 0; i < this.line.getRoute().getRoute().size(); i++){
                if(this.coord.equals(this.line.getRoute().getRoute().get(i))){
                    if(this.forward){
                        if(i+1 >= this.line.getRoute().getRoute().size()){
                            this.forward = false;
                            break;
                        } else {
                            this.target = this.line.getRoute().getRoute().get(i+1);
                            this.targetType = this.line.getRoute().getRouteType().get(i+1);
                            this.travelPieceX = this.target.diffX(this.start) / this.target.distance(this.start);
                            this.travelPieceY = this.target.diffY(this.start) / this.target.distance(this.start);
                            break;
                        }
                    } else {
                        if(i-1 < 0){
                            this.forward = true;
                            break;
                        } else {
                            this.target = this.line.getRoute().getRoute().get(i-1);
                            this.targetType = this.line.getRoute().getRouteType().get(i-1);
                            this.travelPieceX = this.target.diffX(this.start) / this.target.distance(this.start);
                            this.travelPieceY = this.target.diffY(this.start) / this.target.distance(this.start);
                            break;
                        }
                    }
                }
            }
        } else if(this.wait > 0){
            this.wait --;
        } else if(this.coord.distance(this.target) > 2){
            this.exactCoordX = this.exactCoordX + travelPieceX;
            this.exactCoordY = this.exactCoordY + travelPieceY;
            this.coord.setX((int) this.exactCoordX);
            this.coord.setY((int) this.exactCoordY);
            this.vehicle.relocate(this.coord.getX() - 3, this.coord.getY() - 3);
        } else if(this.coord.distance(this.target) <= 2){
            this.start = this.target;
            this.exactCoordX = this.target.getX();
            this.exactCoordY = this.target.getY();
            this.coord.setX(this.target.getX());
            this.coord.setY(this.target.getY());
            this.vehicle.relocate(this.coord.getX() - 3, this.coord.getY() - 3);
            this.target = null;
            if(this.targetType.equals("stop")){
                this.wait = 10;
            }
        }
    }

    public void draw(Pane mapCanvas) {
        if (!this.drawn) {
            this.vehicle.setFill(this.line.getLineColor());
            mapCanvas.getChildren().add(this.vehicle);
            this.drawn = true;
        }
    }

    public void move(int x, int y){
        this.exactCoordX = this.exactCoordX + x;
        this.exactCoordY = this.exactCoordY + y;
        this.coord.setX((int) this.exactCoordX);
        this.coord.setY((int) this.exactCoordY);
        this.vehicle.relocate(this.coord.getX() - 3, this.coord.getY() - 3);
    }

    public void erase(Pane mapCanvas){
        if(this.drawn){
            mapCanvas.getChildren().remove(this.vehicle);
            this.drawn = false;
        }
    }

    public void setForward(boolean setTo){ this.forward = setTo; }

    public boolean getForward(){ return this.forward; }

    public int getStartPosition(){
        for(int i = 0; i < this.line.getRoute().getRoute().size(); i++){
            if(this.start.equals(this.line.getRoute().getRoute().get(i))){
                return i;
            }
        }
        return 0;
    }

}