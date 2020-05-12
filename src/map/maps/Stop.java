package map.maps;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lines.line.PublicTransport;
import lines.line.Timer;
import lines.line.Vehicle;
import map.Imaps.iStop;
import static javafx.scene.Cursor.cursor;

/**
 * Stop class
 * implements interface iStop
 * Used to represent stop in map
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Stop implements iStop{

    /**Coordinates of the stop*/
    private Coordinate coord;
    /**Name of the stop*/
    private java.lang.String name;
    /**Street to which this stop belongs*/
    private Street street;
    /**Tells if the Stop is visible*/
    private boolean drawn;
    /**Graphical stop object*/
    private Rectangle stop = new Rectangle(6, 6);
    /**Public transport connection*/
    private PublicTransport mainPubTrans;
    /**Information pane connection - to display information to*/
    private Pane informationPane;
    /**Map connection*/
    private Map mainMap;
    /**Tells if the stop is currently displaying the information on information Pane*/
    private boolean informationPaneOccupy = false;

    /**
     * Native stop constructor
     * @param name is the name of the stop
     * @param coord is the coordinate of the stop
     * @param mainPubTrans is connection to PublicTransport class
     * @param informationPane is connection to information Pane
     * @param mainMap is connection to Map class
     */
    public Stop(java.lang.String name, Coordinate coord, PublicTransport mainPubTrans, Pane informationPane, Map mainMap){
        this.name = name;
        this.coord = coord;
        this.drawn = false;
        this.stop.setStroke(Color.BLACK);
        this.stop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
        this.stop.relocate(coord.getX() - 3, coord.getY() - 3);
        this.stop.setCursor(cursor("HAND"));
        this.mainPubTrans = mainPubTrans;
        this.informationPane = informationPane;
        this.mainMap = mainMap;
        this.stop.setOnMouseClicked(t -> this.drawInformation());
    }

    /**
     * Returns the Coordinates of the stop
     * @return coordinate of the stop
     */
    public Coordinate getCoord(){ return this.coord; }

    /**
     * Returns the name of the stop
     * @return name of the stop
     */
    public java.lang.String getName(){ return this.name; }

    /**
     * Returns the street to which Stop belongs
     * @return street to which this stop belongs
     */
    public Street getStreet(){ return this.street; }

    /**
     * Sets the street to which stop belongs
     * @param street is the street to which the stop is going to be signed
     */
    public void setStreet(Street street){ this.street = street; }

    /**
     * Makes the stop visible on given Pane
     * @param mapCanvas is the Pane where the stop is going to be drawn
     */
    public void draw(Pane mapCanvas){
        if(!drawn) {
            mapCanvas.getChildren().add(this.stop);
            this.drawn = true;
        }
    }

    /**
     * Erases stop from a given Pane
     * @param mapCanvas is the pane where the stop is going to be erased
     */
    public void erase(Pane mapCanvas){
        if(this.drawn){
            mapCanvas.getChildren().remove(this.stop);
            this.drawn = false;
        }
    }

    /**
     * Moves stop with specified value
     * @param mapCanvas is the Pane where the stop is drawn
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    public void moveStop(Pane mapCanvas, int x, int y){
        this.coord.setMoved();
        this.coord.moveCoord(mapCanvas, x, y);
        if(this.drawn){
            this.erase(mapCanvas);
        }
        this.stop.relocate(coord.getX() - 3, coord.getY() - 3);
        if(this.drawn){
            this.draw(mapCanvas);
        }
    }

    /**
     * Draws information about stop such as stop name, lines that stops at this
     * stop and time that vehicles of some line need to get to this stop
     */
    public void drawInformation(){
        this.mainPubTrans.setAllVehiclesInformationPaneOccupyFalse();
        this.mainMap.setAllStopInformationPaneOccupyFalse();
        this.informationPaneOccupy = true;
        this.informationPane.getChildren().clear();
        Rectangle sidePanel = new Rectangle();
        sidePanel.setX(0);
        sidePanel.setY(0);
        int lines = 0;
        for(int i = 0; i < this.mainPubTrans.getLines().size(); i++){
            int pos = this.mainPubTrans.getLines().get(i).isStopInLineRoute(this.coord);
            if(pos >= 0){
                Rectangle linePanel = new Rectangle();
                linePanel.setFill(this.mainPubTrans.getLines().get(i).getLineColor());
                linePanel.setX(30);
                linePanel.setY(lines * 20);
                linePanel.setHeight(20);
                linePanel.setWidth(170);
                Text lineText = new Text("Line ".concat(String.valueOf(this.mainPubTrans.getLines().get(i).getLineNumber())));
                lineText.setX(35);
                lineText.setY(lines * 20 + 16);
                lines ++;
                this.informationPane.getChildren().addAll(linePanel, lineText);
                for(int j = 0; j < this.mainPubTrans.getLines().get(i).getVehicles().size(); j++){
                    Timer tmpTime = new Timer();
                    Text informationLine = new Text();
                    java.util.List<Integer> tmpSecondsToNextList = this.mainPubTrans.getLines().get(i).getVehicles().get(j).howMuchTimeToNext(pos);
                    int tmpSecondsToNext = tmpSecondsToNextList.get(0);
                    if(tmpSecondsToNext == -1){
                        informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" - ").concat("off route")));
                    } else {
                        int tmpForwardToNext = tmpSecondsToNextList.get(1);
                        tmpTime.addSeconds(tmpSecondsToNext);
                        if(this.mainPubTrans.getLines().get(i).getVehicles().get(j).getStartPosition() == pos &&
                                this.mainPubTrans.getLines().get(i).getVehicles().get(j).getTurns() == 0){
                            if(tmpForwardToNext == 1){
                                informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" > - ").concat("at stop")));
                            } else {
                                informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" < - ").concat("at stop")));
                            }
                        } else {
                            if(tmpForwardToNext == 1){
                                if(this.mainPubTrans.getLines().get(i).getVehicles().get(j).getScheduled()){
                                    if(this.mainPubTrans.getLines().get(i).getVehicles().get(j).howMuchTimeTo(pos) > 0){
                                        informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" < - ").concat(String.valueOf(tmpTime.getHours() * 60 + tmpTime.getMinutes())).concat(" mins")));
                                    } else {
                                        informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" < - departed")));
                                    }
                                } else {
                                    informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" > - ").concat(String.valueOf(tmpTime.getHours() * 60 + tmpTime.getMinutes())).concat(" mins")));
                                }
                            } else {
                                if(this.mainPubTrans.getLines().get(i).getVehicles().get(j).getScheduled()){
                                    if(this.mainPubTrans.getLines().get(i).getVehicles().get(j).howMuchTimeTo(pos) > 0){
                                        informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" < - ").concat(String.valueOf(tmpTime.getHours() * 60 + tmpTime.getMinutes())).concat(" mins")));
                                    } else {
                                        informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" < - departed")));
                                    }
                                } else {
                                    informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" < - ").concat(String.valueOf(tmpTime.getHours() * 60 + tmpTime.getMinutes())).concat(" mins")));
                                }
                            }
                        }
                    }
                    informationLine.setX(35);
                    informationLine.setY(lines * 20 + 16);
                    this.informationPane.getChildren().add(informationLine);
                    lines ++;
                }
                for(int j = 0; j < this.mainPubTrans.getLines().get(i).getScheduledConnections().size(); j++){
                    if(this.mainPubTrans.getLines().get(i).getScheduledConnections().get(j).getVehicleNumber() == -1){
                        Text informationLine = new Text();
                        informationLine.setText("conn. ".concat(String.valueOf(j)));
                        Vehicle tmpVehicle;
                        if(this.mainPubTrans.getLines().get(i).getScheduledConnections().get(j).getVehicleForward()){
                            tmpVehicle = new Vehicle(this.mainPubTrans.getLines().get(i),
                                    this.mainPubTrans.getLines().get(i).getRoute().getRoute().get(0),
                                    0, this.mainMap, this.mainPubTrans, null);
                            informationLine.setText(informationLine.getText().concat(" > - "));
                        } else {
                            tmpVehicle = new Vehicle(this.mainPubTrans.getLines().get(i),
                                    this.mainPubTrans.getLines().get(i).getRoute().getRoute().get(this.mainPubTrans.getLines().get(i).getRoute().getRoute().size() -1),
                                    0, this.mainMap, this.mainPubTrans, null);
                            tmpVehicle.setForward(false);
                            informationLine.setText(informationLine.getText().concat(" < - "));
                        }
                        Timer tmpTimer = new Timer();
                        tmpTimer.set(this.mainPubTrans.getLines().get(i).getScheduledConnections().get(j).getDepartureTime().getSeconds(),
                                this.mainPubTrans.getLines().get(i).getScheduledConnections().get(j).getDepartureTime().getMinutes(),
                                this.mainPubTrans.getLines().get(i).getScheduledConnections().get(j).getDepartureTime().getHours());
                        tmpTimer.addSeconds(tmpVehicle.howMuchTimeTo(pos));
                        informationLine.setText(informationLine.getText().concat(String.valueOf(this.mainPubTrans.getTimer().minutesTo(tmpTimer))).concat(" mins"));
                        informationLine.setX(35);
                        informationLine.setY(lines * 20 + 16);
                        this.informationPane.getChildren().add(informationLine);
                        lines ++;
                    }
                }
            }
        }
        sidePanel.setWidth(30);
        if(lines * 20 < 300){
            sidePanel.setHeight(300);
            this.informationPane.setMinHeight(300);
        } else {
            sidePanel.setHeight(lines * 20);
            this.informationPane.setMinHeight(lines * 20);
        }
        sidePanel.setFill(Paint.valueOf("CADETBLUE"));
        Text stopText = new Text("Stop ".concat(this.name));
        stopText.setRotate(-90);
        stopText.setY(100);
        stopText.setX(-40);
        this.informationPane.getChildren().addAll(sidePanel, stopText);
    }

    /**
     * Returns if the information in the information panel is being displayed
     * @return true if stop is displaying its information
     */
    public boolean getInformationPaneOccupy(){ return this.informationPaneOccupy; }

    /**
     * Sets information pane occupation to false
     */
    public void InformationPaneOccupyFalse(){ this.informationPaneOccupy = false; }

    /**
     * Prepares the stop for removal
     */
    public void removeProcedure(){
        this.informationPane.getChildren().clear();
        Text removeText = new Text("Stop ".concat(this.name).concat("\nhas been removed\nclick on other stop\nor vehicle"));
        this.informationPane.getChildren().add(removeText);
        removeText.setX(20);
        removeText.setY(50);
    }
}