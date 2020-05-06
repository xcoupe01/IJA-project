package map.maps;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lines.line.PublicTransport;
import lines.line.Timer;
import map.Imaps.iStop;
import static javafx.scene.Cursor.cursor;

public class Stop implements iStop{

    private Coordinate coord;                              //< Coordinates of the stop
    private java.lang.String name;                         //< name of the stop
    private Street street;                                 //< Street to which this stop belongs
    private boolean drawn;                                 //< tells if the Stop is visible
    private Rectangle stop = new Rectangle(6, 6);   //< graphical stop object
    private PublicTransport mainPubTrans;
    private Pane informationPane;
    private Map mainMap;
    private boolean informationPaneOccupy = false;

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

    // returns the Coordinates of the stop
    public Coordinate getCoord(){ return this.coord; }

    // returns the name of the stop
    public java.lang.String getName(){ return this.name; }

    // returns the street to which Stop belongs
    public Street getStreet(){ return this.street; }

    // sets the street to which Stop belongs
    public void setStreet(Street street){ this.street = street; }

    // makes the Stop visible on given Pane
    public void draw(Pane mapCanvas){
        if(!drawn) {
            mapCanvas.getChildren().add(this.stop);
            this.drawn = true;
        }
    }

    // erases stop from mapCanvas
    public void erase(Pane mapCanvas){
        if(this.drawn){
            mapCanvas.getChildren().remove(this.stop);
            this.drawn = false;
        }
    }

    // moves stop with specified value
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
                    tmpTime.addSeconds(this.mainPubTrans.getLines().get(i).getVehicles().get(j).howMuchTimeToNext(pos));
                    Text informationLine = new Text();
                    if(this.mainPubTrans.getLines().get(i).getVehicles().get(j).getStartPosition() == pos &&
                            this.mainPubTrans.getLines().get(i).getVehicles().get(j).getTurns() == 0){
                        informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" - ").concat("at stop")));
                    } else {
                        informationLine.setText(("vehicle ".concat(String.valueOf(j+1)).concat(" - ").concat(String.valueOf(tmpTime.getHours() * 60 + tmpTime.getMinutes())).concat(" mins")));
                    }
                    informationLine.setX(35);
                    informationLine.setY(lines * 20 + 16);
                    this.informationPane.getChildren().add(informationLine);
                    lines ++;
                }
            }
        }
        sidePanel.setWidth(30);
        sidePanel.setHeight(lines * 20 + 320);
        sidePanel.setFill(Paint.valueOf("CADETBLUE"));
        Text stopText = new Text("Stop ".concat(this.name));
        stopText.setRotate(-90);
        stopText.setY(100);
        stopText.setX(-40);
        this.informationPane.getChildren().addAll(sidePanel, stopText);
    }

    public boolean getInformationPaneOccupy(){ return this.informationPaneOccupy; }
    public void InformationPaneOccupyFalse(){ this.informationPaneOccupy = false; }

}