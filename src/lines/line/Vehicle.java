package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import lines.Iline.iVehicle;
import map.maps.Coordinate;
import map.maps.Map;

import static javafx.scene.Cursor.cursor;

/**
 * Vehicle class
 * implements interface iVehicle
 * Used to represent vehicles in public transport
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Vehicle implements iVehicle {

    /** Graphical object of vehicle*/
    private Circle vehicle = new Circle(3);
    /** Graphical object of the vehicle in the information Pane*/
    private Circle informationPanelVehicle = new Circle(3);
    /** Line to which the vehicle belongs*/
    private PTLine line;
    /** Actual vehicle coordinate*/
    private Coordinate coord = new Coordinate(0,0);
    /** Position from where the vehicle started actual ride (newest point of the line route where the vehicle was)*/
    private Coordinate start;
    /** Position where the vehicle is heading*/
    private Coordinate target = null;
    /** Type of target where the vehicle is heading ("point" or "stop")*/
    private String targetType;
    /** Piece that the vehicle is going to travel every step of the ride on X axis*/
    private double travelPieceX;
    /** Piece that the vehicle is going to travel every step of the ride on Y axis*/
    private double travelPieceY;
    /** Actual position of the vehicle on X axis (in double, in coord is less accurate)*/
    private double exactCoordX;
    /** Actual position of the vehicle on Y axis (in double, in coord is less accurate)*/
    private double exactCoordY;
    /** List index of the line route for vehicle*/
    private int informationPaneCounter;
    /** Actual position of the vehicle in information Pane*/
    private double exactInformationCoordY;
    /** Piece that the vehicle is going to travel every step of the ride on the information Pane*/
    private double informationTravelPiece;
    /** Orientation of the vehicle*/
    private boolean forward = true;
    /** Wait value of the vehicle*/
    private int wait = 0;
    /** Tells if the vehicle is visible on map Pane*/
    private boolean drawn = false;
    /** Tells how many steps have been done from last route point*/
    private int turns = 0;
    /** Connection to information Pane*/
    private Pane informationPane = null;
    /** Vehicle number*/
    private int vehicleNumber;
    /** Connection to map*/
    private Map mainMap;
    /** Connection to public transport*/
    private PublicTransport mainPubTrans;
    /** Tells how many seconds means one turn*/
    private int turnMeansSec = 10;
    /** Tells how many steps vehicle waits at stop*/
    private int turnsAtStop = 20;
    /** Tells if vehicle occupies information Pane*/
    private boolean informationPaneOccupy = false;
    /***/
    private boolean offRoad = false;

    /**
     * Native constructor of Vehicle class
     * @param line is the line to which the vehicle belongs
     * @param start is the starting point of the vehicle
     * @param vehicleNumber is the vehicle number
     * @param mainMap is connection to map
     * @param mainPubTrans is connection to public transport
     */
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

    /**
     * Makes a step of vehicle
     */
    public void ride(){
        boolean notOK = true;
        if(this.target == null){
            for(int i = 0; i < this.line.getRoute().getRoute().size(); i++){
                if(this.coord.equals(this.line.getRoute().getRoute().get(i))){
                    if(this.forward){
                        if(i+1 >= this.line.getRoute().getRoute().size()){
                            this.forward = false;
                            notOK = false;
                            if(this.informationPaneOccupy){
                                this.drawInformation();
                            }
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
                            notOK = false;
                            if(this.informationPaneOccupy){
                                this.drawInformation();
                            }
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
            if(this.target == null && notOK){
                double distance = Double.POSITIVE_INFINITY;
                Coordinate tmpCoord = null;
                int tmpCounter = 0;
                for(int i = 0; i < this.line.getRoute().getRoute().size(); i++){
                    if(this.coord.distance(this.line.getRoute().getRoute().get(i)) < distance){
                        tmpCoord = this.line.getRoute().getRoute().get(i);
                        distance = this.coord.distance(this.line.getRoute().getRoute().get(i));
                        tmpCounter = i;
                    }
                }
                assert tmpCoord != null;
                this.coord.setX(tmpCoord.getX());
                this.coord.setY(tmpCoord.getY());
                this.start = tmpCoord;
                this.exactCoordX = tmpCoord.getX();
                this.exactCoordY = tmpCoord.getY();
                this.informationPaneCounter = tmpCounter;
            }
        } else if(this.wait > 0){
            this.wait --;
        } else if(this.coord.distance(this.target) > 1){
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
        } else if(this.coord.distance(this.target) <= 1){
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
                this.wait = this.turnsAtStop;
            }
        }
    }

    /**
     * Draws the vehicle to given Pane
     * @param mapCanvas is the Pane where the vehicle is going to be drawn
     */
    public void draw(Pane mapCanvas) {
        if (!this.drawn) {
            this.vehicle.setFill(this.line.getLineColor());
            mapCanvas.getChildren().add(this.vehicle);
            this.drawn = true;
        }
    }

    /**
     * Erases the vehicle from a given Pane
     * @param mapCanvas is the Pane where the vehicle is going to be erased
     */
    public void erase(Pane mapCanvas){
        if(this.drawn){
            mapCanvas.getChildren().remove(this.vehicle);
            this.drawn = false;
        }
    }

    /**
     * Moves the vehicle with given values
     * @param x is the movement amount in X axis
     * @param y is the movement amount in Y axis
     */
    public void move(int x, int y){
        this.exactCoordX = this.exactCoordX + x;
        this.exactCoordY = this.exactCoordY + y;
        this.coord.setX((int) this.exactCoordX);
        this.coord.setY((int) this.exactCoordY);
        this.vehicle.relocate(this.coord.getX() - 3, this.coord.getY() - 3);
    }

    /**
     * Sets the orientation of vehicle
     * @param setTo true means forward false means backward
     */
    public void setForward(boolean setTo){ this.forward = setTo; }

    /**
     * Tell the orientation of vehicle
     * @return true if forward false if backward
     */
    public boolean getForward(){ return this.forward; }

    /**
     * Returns the last point from the line route where the vehicle was
     * @return the list index of the last position of the line route where the vehicle was
     */
    public int getStartPosition(){
        for(int i = 0; i < this.line.getRoute().getRoute().size(); i++){
            if(this.start.equals(this.line.getRoute().getRoute().get(i))){
                return i;
            }
        }
        return 0;
    }

    /**
     * Tells how many steps the vehicle did from last route point
     * @return the amount of steps the vehicle did from last route point
     */
    public int getTurns(){ return this.turns; }

    /**
     * Attach the information Pane for the vehicle
     * @param informationPane the Pane to be attached
     */
    public void setInformationPane(Pane informationPane){
        this.informationPane = informationPane;
    }

    /**
     * Draws vehicle schedule on the information Pane
     */
    public void drawInformation(){
        this.mainPubTrans.setAllVehiclesInformationPaneOccupyFalse();
        this.mainMap.setAllStopInformationPaneOccupyFalse();
        this.informationPaneOccupy = true;
        this.informationPane.getChildren().clear();
        this.informationPane.setMinHeight((this.line.getRoute().getRoute().size() * 30));
        this.informationPane.getChildren().add(this.informationPanelVehicle);
        Rectangle sidePanel = new Rectangle();
        sidePanel.setX(0);
        sidePanel.setY(0);
        if((this.line.getRoute().getRoute().size() * 30) + 20 > 320){
            sidePanel.setHeight((this.line.getRoute().getRoute().size() * 30) + 20);
        } else {
            sidePanel.setHeight(320);
        }
        sidePanel.setWidth(30);
        sidePanel.setFill(this.line.getLineColor());
        Text lineText = new Text("Vehicle ".concat(String.valueOf(this.vehicleNumber).concat(" Line ".concat(String.valueOf(this.line.getLineNumber())))));
        Text failText = new Text("Vehicle of road \ndetected");
        failText.setX(50);
        failText.setY(100);
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
                Text tmpStopTime = new Text();
                Timer tmpTimer = new Timer();
                tmpTimer.set(this.mainPubTrans.getTimer().getSeconds(), this.mainPubTrans.getTimer().getMinutes(), this.mainPubTrans.getTimer().getHours());
                tmpTimer.addSeconds(this.howMuchTimeTo(i));
                if(this.offRoad){
                    this.informationPane.getChildren().clear();
                    this.informationPane.getChildren().addAll(sidePanel, lineText, failText);
                    this.informationPane.setMinHeight(300);
                    break;
                }
                if(tmpTimer.getMinutes() < 10){
                    tmpStopTime.setText(String.valueOf(tmpTimer.getHours()).concat(":0").concat(String.valueOf(tmpTimer.getMinutes())));
                } else {
                    tmpStopTime.setText(String.valueOf(tmpTimer.getHours()).concat(":").concat(String.valueOf(tmpTimer.getMinutes())));
                }
                tmpStopTime.relocate(40, 30 * i + 5);
                this.informationPane.getChildren().addAll(tmpStop, tmpStopName, tmpStopTime);
            } else {
                Circle tmpPoint = new Circle(3);
                tmpPoint.setStroke(Color.BLACK);
                tmpPoint.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
                tmpPoint.relocate(80, 30 * i + 10);
                Timer tmpTimer = new Timer();
                tmpTimer.set(this.mainPubTrans.getTimer().getSeconds(), this.mainPubTrans.getTimer().getMinutes(), this.mainPubTrans.getTimer().getHours());
                tmpTimer.addSeconds(this.howMuchTimeTo(i));
                if(this.offRoad){
                    this.informationPane.getChildren().clear();
                    this.informationPane.getChildren().addAll(sidePanel, lineText, failText);
                    this.informationPane.setMinHeight(300);
                    break;
                }
                Text tmpPointTime = new Text();
                if(tmpTimer.getMinutes() < 10){
                    tmpPointTime.setText(String.valueOf(tmpTimer.getHours()).concat(":0").concat(String.valueOf(tmpTimer.getMinutes())));
                } else {
                    tmpPointTime.setText(String.valueOf(tmpTimer.getHours()).concat(":").concat(String.valueOf(tmpTimer.getMinutes())));
                }
                tmpPointTime.relocate(40, 30 * i + 5);
                this.informationPane.getChildren().addAll(tmpPoint, tmpPointTime);
            }
        }
        this.informationPanelVehicle.relocate(80, this.exactInformationCoordY);
    }

    /**
     * Tell how much time the vehicle needs to arrive to position in current ride. It also gives
     * negative values for stops that has already been visited this ride.
     * @param pos is the list index to line route coordinate list
     * @return amount in app seconds that the vehicle need to get to position (can be negative)
     */
    public int howMuchTimeTo(int pos) {
        this.offRoad = false;
        int tmpSeconds = 0;
        if (this.turns > 0) {
            tmpSeconds -= this.turns * this.turnMeansSec;
            if((this.forward && pos < this.informationPaneCounter) || (!this.forward && pos > this.informationPaneCounter)){
                tmpSeconds -= this.turnsAtStop * this.turnMeansSec;
            }
        } else if(this.line.getRoute().getRouteType().get(this.informationPaneCounter).equals("stop")){
            tmpSeconds += (this.wait) * this.turnMeansSec;
        }
        if(this.informationPaneCounter >= this.line.getRoute().getRoute().size()){
            this.offRoad = true;
            return 0;
        }
        Vehicle tmpVehicle = new Vehicle(this.line, this.line.getRoute().getRoute().get(this.informationPaneCounter), 0, this.mainMap, this.mainPubTrans);
        if(pos < this.informationPaneCounter){
            tmpVehicle.setForward(false);
        }
        int steps = 0;
        while(tmpVehicle.getStartPosition() != pos){
            steps ++;
            tmpVehicle.ride();
        }
        if((this.forward && pos < this.informationPaneCounter) || (!this.forward && pos > this.informationPaneCounter)){
            steps = - steps;
        }
        return (steps * this.turnMeansSec) + tmpSeconds;
    }

    /**
     * Tell how much time the vehicle needs to arrive to position. When everything is OK it returns only positive values
     * how many time it will take for the vehicle to get to stop. When error occures (when vehicle is not on route) it returns -1
     * @param pos is the list index to line route coordinate list
     * @return amount in app seconds that the vehicle need to get to position, when error it returns -1
     */
    public int howMuchTimeToNext(int pos){
        int tmpSeconds = 0;
        if(this.turns > 0){
            tmpSeconds -= this.turns * this.turnMeansSec;
        }
        if(this.wait > 0){
            tmpSeconds += this.wait * this.turnMeansSec;
        }
        if(this.informationPaneCounter >= this.line.getRoute().getRoute().size()){
            return -1;
        }
        Vehicle tmpVehicle = new Vehicle(this.line, this.line.getRoute().getRoute().get(this.informationPaneCounter), 0, this.mainMap, this.mainPubTrans);
        tmpVehicle.setForward(this.forward);
        int steps = 0;
        if(this.turns > 0 && tmpVehicle.getStartPosition() == pos ){
            while(tmpVehicle.getStartPosition() == pos){
                steps ++;
                tmpVehicle.ride();
            }
        }
        while(tmpVehicle.getStartPosition() != pos){
            steps ++;
            tmpVehicle.ride();
        }
        return (steps * this.turnMeansSec) + tmpSeconds;

    }

    /**
     * Sets amount of seconds that means one turn
     * @param num is the value that's being set
     */
    public void setTurnMeansSec(int num){ this.turnMeansSec = num; }

    /**
     * Sets how many turns vehicle should wait at stop
     * @param num is the value how many turns the vehicle should wait
     */
    public void setTurnsAtStop(int num){ this.turnsAtStop = num; }

    /**
     * Tells if this vehicle occupy the information Pane
     * @return true if vehicle occupies the information Pane false otherwise
     */
    public boolean getInformationPaneOccupy(){ return this.informationPaneOccupy; }

    /**
     * Sets information Pane occupation value to false
     */
    public void setInformationPaneOccupyFalse(){ this.informationPaneOccupy = false; }

    /**
     * Returns the coordinate of the starting point of the vehicle
     * @return the coordinate of the starting point of the vehicle
     */
    public Coordinate getStartCoordinate(){ return this.start; }

    /**
     * Switches the orientation of the vehicle anywhere on the route. It does not just
     * switch the "forward" value but rather prepares it for going other direction.
     */
    public void switchDirection(){
        if(this.wait > 0){
            int tmpWait = this.turnsAtStop - this.wait;
            this.wait = 0;
            this.forward = !this.forward;
            this.target = this.start;
            this.ride();
            this.wait = tmpWait;
        } else {
            Vehicle tmpVehicle = new Vehicle(this.line, this.start, 0, this.mainMap, this.mainPubTrans);
            tmpVehicle.setForward(this.forward);
            int counter = 0;
            while(tmpVehicle.getStartCoordinate().equals(this.target)){
                tmpVehicle.ride();
                counter++;
            }
            this.targetType = this.line.getRoute().getRouteType().get(this.informationPaneCounter);
            if(this.forward){
                this.informationPaneCounter ++;
            } else {
                this.informationPaneCounter --;
            }
            this.turns = counter - this.turns;
            this.travelPieceX = - this.travelPieceX;
            this.travelPieceY = - this.travelPieceY;
            this.forward = !this.forward;
            Coordinate tmpCoord = this.start;
            this.start = this.target;
            this.target = tmpCoord;
        }
    }

    /**
     * Returns current vehicle position
     * @return current vehicle position
     */
    public Coordinate getPosition(){ return this.coord; }

    /**
     * Prepares the vehicle for removal
     */
    public void removeProcedure(){
        this.informationPane.getChildren().clear();
        Text removeText = new Text("vehicle ".concat(String.valueOf(this.vehicleNumber)).concat(" from line ")
                .concat(String.valueOf(this.line.getLineNumber())).concat("\nhas been removed\nclick on other stop\nor vehicle"));
        this.informationPane.getChildren().add(removeText);
        removeText.setX(20);
        removeText.setY(50);
    }

}