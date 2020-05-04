package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import lines.Iline.iVehicle;
import map.maps.Coordinate;
import map.maps.Map;

import static javafx.scene.Cursor.cursor;

public class Vehicle implements iVehicle {
    private Circle vehicle = new Circle(3);
    private Circle informationPanelVehicle = new Circle(3);
    private PTLine line;
    private Coordinate coord = new Coordinate(0,0);
    private Coordinate start;
    private Coordinate target = null;
    private String targetType;
    private double travelPieceX;
    private double travelPieceY;
    private double exactCoordX;
    private double exactCoordY;
    private int informationPaneCounter;
    private double exactInformationCoordY;
    private double informationTravelPiece;
    private boolean forward = true;
    private int wait = 0;
    private boolean drawn = false;
    private int turns = 0;
    private Pane informationPane = null;
    private int vehicleNumber;
    private Map mainMap;
    private PublicTransport mainPubTrans;

    Vehicle(PTLine line, Coordinate start, int vehicleNumber, Map mainMap, PublicTransport mainPubTrans){
        this.line = line;
        this.vehicle.setStroke(Color.BLACK);
        this.vehicle.setFill(this.line.getLineColor());
        this.informationPanelVehicle.setStroke(Color.BLACK);
        this.informationPanelVehicle.setFill(this.line.getLineColor());
        for (int i = 0; i < this.line.getRoute().getRouteType().size(); i++){
            if(start.equals(this.line.getRoute().getRoute().get(i))){
                this.informationPaneCounter = i;
                this.exactInformationCoordY = 30 * i + 10;
                break;
            }
        }
        this.start = start;
        this.coord.setX(start.getX());
        this.coord.setY(start.getY());
        this.exactCoordX = start.getX();
        this.exactCoordY = start.getY();
        this.vehicle.relocate(this.coord.getX() - 3, this.coord.getY() - 3);
        this.vehicle.setOnMouseClicked(event -> this.drawInformation());
        this.vehicle.setCursor(cursor("HAND"));
        this.vehicleNumber = vehicleNumber;
        this.mainMap = mainMap;
        this.mainPubTrans = mainPubTrans;
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
                            this.informationTravelPiece = 30 / this.target.distance(this.start);
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
                            this.informationTravelPiece = 30 / this.target.distance(this.start);
                            break;
                        }
                    }
                }
            }
        } else if(this.wait > 0){
            this.wait --;
        } else if(this.coord.distance(this.target) > 2){
            this.turns ++;
            this.exactCoordX = this.exactCoordX + this.travelPieceX;
            this.exactCoordY = this.exactCoordY + this.travelPieceY;
            this.coord.setX((int) this.exactCoordX);
            this.coord.setY((int) this.exactCoordY);
            this.vehicle.relocate(this.coord.getX() - 3, this.coord.getY() - 3);
            if(this.forward){
                this.exactInformationCoordY = this.exactInformationCoordY + this.informationTravelPiece;
            } else {
                this.exactInformationCoordY = this.exactInformationCoordY - this.informationTravelPiece;
            }
            this.informationPanelVehicle.relocate(80, this.exactInformationCoordY);
        } else if(this.coord.distance(this.target) <= 2){
            if(this.forward){
                this.informationPaneCounter ++;
            } else {
                this.informationPaneCounter --;
            }
            this.exactInformationCoordY = 30 * this.informationPaneCounter + 10;
            this.turns = 0;
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

    public int getTurns(){ return this.turns; }
    public void setInformationPane(Pane informationPane){
        this.informationPane = informationPane;
    }
    public void drawInformation(){
        this.informationPane.getChildren().clear();
        this.informationPane.setMinHeight((this.line.getRoute().getRoute().size() * 30));
        Rectangle sidePanel = new Rectangle();
        sidePanel.setX(0);
        sidePanel.setY(0);
        sidePanel.setHeight((this.line.getRoute().getRoute().size() * 30) + 20);
        sidePanel.setWidth(30);
        sidePanel.setFill(this.line.getLineColor());
        Text lineText = new Text("Vehicle ".concat(String.valueOf(this.vehicleNumber).concat(" Line ".concat(String.valueOf(this.line.getLineNumber())))));
        lineText.setRotate(-90);
        lineText.setY(100);
        lineText.setX(-40);
        this.informationPane.getChildren().addAll(sidePanel, lineText);
        for(int i = 0; i < this.line.getRoute().getRoute().size(); i++){
            if(i > 0){
                Line tmpLine = new Line(83, 30 * (i - 1) + 16, 83, 30 * i + 10);
                tmpLine.setStrokeWidth(1);
                tmpLine.setStroke(Color.BLACK.deriveColor(1, 1, 1, 0.7));
                this.informationPane.getChildren().add(tmpLine);
            }
            if(this.line.getRoute().getRouteType().get(i).equals("stop")){
                Rectangle tmpStop = new Rectangle(6, 6);
                tmpStop.setStroke(Color.BLACK);
                tmpStop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
                tmpStop.relocate(80, 30 * i + 10);
                Text tmpStopName = new Text(this.mainMap.getStopByCoord(this.line.getRoute().getRoute().get(i)).getName());
                tmpStopName.relocate(90, 30 * i + 5);
                Text tmpStopTime = new Text(String.valueOf(this.mainPubTrans.getTimeHours()).concat(":").concat(String.valueOf(this.mainPubTrans.getTimeMinutes())));
                tmpStopTime.relocate(40, 30 * i + 5);
                this.informationPane.getChildren().addAll(tmpStop, tmpStopName, tmpStopTime);
            } else {
                Circle tmpPoint = new Circle(3);
                tmpPoint.setStroke(Color.BLACK);
                tmpPoint.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
                tmpPoint.relocate(80, 30 * i + 10);
                Text tmpPointTime = new Text(String.valueOf(this.mainPubTrans.getTimeHours()).concat(":").concat(String.valueOf(this.mainPubTrans.getTimeMinutes())));
                tmpPointTime.relocate(40, 30 * i + 5);
                this.informationPane.getChildren().addAll(tmpPoint, tmpPointTime);
            }
        }
        this.informationPanelVehicle.relocate(80, this.exactInformationCoordY);
        this.informationPane.getChildren().add(this.informationPanelVehicle);
    }

}