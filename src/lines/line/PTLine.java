package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.Iline.iPTLine;
import map.maps.Coordinate;
import map.maps.Map;

/**
 * PTLine class
 * implements interface iPTLine
 * Used to represent lines in public transport
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class PTLine implements iPTLine {

    /** Route of the line*/
    private Route lineRoute;
    /** List of vehicles of the line*/
    private java.util.List<Vehicle> lineVehicles = new java.util.ArrayList<>();
    /** Color of the line*/
    private Color lineColor;
    /** Number of the line*/
    private int lineNumber;
    /** Tells if the line highlight is visible*/
    private boolean drawn = false;
    /** Connection to map*/
    private Map mainMap;

    /**
     * Native constructor of PTLine class
     * @param lineNumber is the number of the line
     * @param myColor is the color of the line
     * @param map is map connection
     */
    public PTLine(int lineNumber,Color myColor, Map map){
        this.lineColor = myColor;
        this.lineNumber = lineNumber;
        this.lineRoute = new Route(map, this);
        this.mainMap = map;
    }

    /**
     * Adds route to the line
     * @param toSet is the route to be assigned to the route
     */
    public void setRoute(Route toSet){ this.lineRoute = toSet;}

    /**
     * Returns the route of the line
     * @return the route of the line
     */
    public Route getRoute(){ return this.lineRoute;}

    /**
     * Tells the line color
     * @return the line color
     */
    public Color getLineColor(){ return this.lineColor; }

    /**
     * Tells the line number
     * @return the line number
     */
    public int getLineNumber(){ return this.lineNumber; }

    /**
     * Adds vehicle to the line
     * @param linePoint is where the vehicle is going to be added
     * @param mainPubTrans connection to public transport
     */
    public void addVehicle(int linePoint, PublicTransport mainPubTrans){
        Vehicle newVehicle = new Vehicle(this, this.lineRoute.getRoute().get(linePoint), this.lineVehicles.size() + 1, mainMap, mainPubTrans);
        this.lineVehicles.add(newVehicle);
    }

    /**
     * Draws all line vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be drawn
     */
    public void drawVehicles(Pane mapCanvas){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.draw(mapCanvas);
        }
    }

    /**
     * Erases all line vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be erased
     */
    public void eraseVehicles(Pane mapCanvas){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.erase(mapCanvas);
        }
    }

    /**
     * Draws line route highlight on a given Pane
     * @param mapCanvas is the Pane where the line route highlight is going to be drawn
     */
    public void drawLineHighlight(Pane mapCanvas){
        if(!this.drawn){
            this.drawn = true;
            this.lineRoute.draw(mapCanvas);
        }
    }

    /**
     * Erases line route highlight on a given Pane
     * @param mapCanvas is the Pane where the line route is going to be erased
     */
    public void eraseLineHighlight(Pane mapCanvas){
        if(drawn){
            this.drawn = false;
            this.lineRoute.erase(mapCanvas);
        }
    }

    /**
     * Toggles the line route highlight on a given Pane
     * @param mapCanvas is the Pane where the line route is going to be toggled
     */
    public void toggleLineHighlight(Pane mapCanvas) {
        if(this.drawn){
            this.eraseLineHighlight(mapCanvas);
        } else {
            this.drawLineHighlight(mapCanvas);
        }
    }

    /**
     * Makes one animation step with all vehicles
     */
    public void rideAllVehicles(){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.ride();
        }
    }

    /**
     * Moves all objects of a line
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    public void updateVehicles(int x, int y){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.move(x, y);
        }
    }

    /**
     * Sets all vehicles value that represents how many seconds means one animation step in application time
     * @param num the value that represents how many seconds means one animation step in application time
     */
    public void setVehiclesTickMeansSec(int num){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.setTurnMeansSec(num);
        }
    }

    /**
     * Sets how many animation steps vehicle will wait at stop
     * @param num is the number of steps that vehicle waits at stop
     */
    public void setVehiclesTicksAtStop(int num){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.setTurnsAtStop(num);
        }
    }

    /**
     * Tells list index of stop of the line if the given coordinates matches the position of the stop
     * @param stopCoord is the coordinate to be searched for
     * @return list index of a stop that matches the given coordinates -1 if any of the stops matches
     */
    public int isStopInLineRoute(Coordinate stopCoord){
        for(int i = 0; i < this.lineRoute.getRoute().size(); i++){
            if(this.lineRoute.getRoute().get(i).equals(stopCoord) && this.lineRoute.getRouteType().get(i).equals("stop")){
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the list of all line vehicles
     * @return the list of all line vehicles
     */
    public java.util.List<Vehicle> getVehicles(){ return this.lineVehicles;}
}