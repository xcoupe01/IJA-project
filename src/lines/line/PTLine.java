package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.Iline.iPTLine;
import map.maps.Coordinate;
import map.maps.Map;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    /** List of scheduled Connections*/
    private java.util.List<PTConnection> scheduledConnections = new java.util.ArrayList<>();


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
     * Adds vehicle to the line, it makes sure that there are not two vehicles on the road with the same number
     * @param linePoint is where the vehicle is going to be added
     * @param mainPubTrans connection to public transport
     * @param mapCanvas is the map Pane where the vehicle will be visible
     */
    public void addVehicle(int linePoint, PublicTransport mainPubTrans, Pane mapCanvas){
        for(int i = 1; true; i++){
            if(this.getVehicleByNumber(i) == null){
                Vehicle newVehicle = new Vehicle(this, this.lineRoute.getRoute().get(linePoint), i, mainMap, mainPubTrans, mapCanvas);
                this.lineVehicles.add(newVehicle);
                return;
            }
        }
    }

    /**
     * Draws all line vehicles
     */
    public void drawVehicles(){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.draw();
        }
    }

    /**
     * Erases all line vehicles
     */
    public void eraseVehicles(){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.erase();
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

    /**
     * Removes the closest vehicle from the line to given point
     * @param mouseCoord is the coordinate that defines which vehicle is going to be erased
     * @param mapCanvas is the map Pane where the vehicle is going to be deleted (erased)
     */
    public void removeNearestVehicle(Coordinate mouseCoord, Pane mapCanvas){
        double distance = Double.POSITIVE_INFINITY;
        int targetVehicle = 0;
        if(this.lineVehicles.size() == 0){
            return;
        }
        for(int i = 0; i < this.lineVehicles.size(); i++){
            if(this.lineVehicles.get(i).getPosition().distance(mouseCoord) < distance){
                distance = this.lineVehicles.get(i).getPosition().distance(mouseCoord);
                targetVehicle = i;
            }
        }
        this.lineVehicles.get(targetVehicle).erase();
        this.lineVehicles.get(targetVehicle).removeProcedure();
        this.lineVehicles.remove(targetVehicle);
    }

    /**
     * Returns the closest vehicle to a given point
     * @param mouseCoord is the coordinate that defines which vehicle is going to be returned
     * @return the closest vehicle to a given point
     */
    public Vehicle getNearestVehicle(Coordinate mouseCoord){
        double distance = Double.POSITIVE_INFINITY;
        int targetVehicle = 0;
        if(this.lineVehicles.size() == 0){
            return null;
        }
        for(int i = 0; i < this.lineVehicles.size(); i++){
            if(this.lineVehicles.get(i).getPosition().distance(mouseCoord) < distance){
                distance = this.lineVehicles.get(i).getPosition().distance(mouseCoord);
                targetVehicle = i;
            }
        }
        return this.lineVehicles.get(targetVehicle);
    }

    /**
     * Finds vehicle by vehicle number
     * @param vehicleNumber is the vehicle number to be searched for
     * @return the vehicle with given number or null if not found
     */
    public Vehicle getVehicleByNumber(int vehicleNumber){
        for (Vehicle lineVehicle : this.lineVehicles) {
            if (lineVehicle.getVehicleNumber() == vehicleNumber) {
                return lineVehicle;
            }
        }
        return null;
    }

    /**
     * Removes vehicle with given vehicle number
     * @param vehicleNumber the vehicle number of vehicle to be removed
     */
    public void removeVehicleByNumber(int vehicleNumber){
        for(int i = 0; i < this.lineVehicles.size(); i++){
            if(this.lineVehicles.get(i).getVehicleNumber() == vehicleNumber){
                this.lineVehicles.remove(i);
                break;
            }
        }
    }

    /**
     * Adds a scheduled connection to a list of this connections of this line
     * @param toAdd connection to be added
     */
    public void addScheduledConnection(PTConnection toAdd){ this.scheduledConnections.add(toAdd); }

    /**
     * returns a list of scheduled connections on this line
     * @return list of connections on this line
     */
    public java.util.List<PTConnection> getScheduledConnections(){ return this.scheduledConnections; }

    /**
     * checks if any of scheduled connection should depart or arrive (also works when the time is going backwards
     */
    public void tickCheckScheduledConnections(){
        for (PTConnection scheduledConnection : this.scheduledConnections) {
            scheduledConnection.tickCheck();
        }
    }

    /**
     * Refreshes all scheduled connections arrival times and active value
     */
    public void refreshArrivalTimes(){
        for (PTConnection scheduledConnection : this.scheduledConnections) {
            scheduledConnection.refreshArrivalTime();
        }
    }

    /**
     * Sets drawn value for all vehicles in the line
     * @param set the value that's set
     */
    public void setVehiclesDrawn(boolean set){
        for (Vehicle lineVehicle : this.lineVehicles) {
            lineVehicle.setDrawn(set);
        }
    }

    /**
     * Removes scheduled connection with given index
     * @param index list index of connection to be removed
     */
    public void removeScheduledConnection(int index){
        if(index < this.scheduledConnections.size()){
            this.scheduledConnections.get(index).removeProcedure();
            this.scheduledConnections.remove(index);
        }
    }

    /**
     * Generates line time table based on scheduled lines in PNG format
     * @param filepath is the folder to be saved to
     * @param mainPubTrans is the connection for the public transport
     */
    public void generateTimeScheduleToFile(String filepath, PublicTransport mainPubTrans){

        java.util.List<Integer> stopX = new java.util.ArrayList<>();
        int horizontalShift = 150;
        int width = 0;
        for(int i = 0; i < this.lineRoute.getRouteType().size(); i++){
            if(this.lineRoute.getRouteType().get(i).equals("stop")){
                width ++;
            }
        }
        width = (width * 120) + horizontalShift;
        int height = (this.scheduledConnections.size() * 50) + 100;
        // vertical spacing is 50 point per line + 2 initial lines
        java.util.List<Integer> timesForward = new java.util.ArrayList<>();
        java.util.List<Integer> timesBackward = new java.util.ArrayList<>();
        Vehicle tmpVehicle = new Vehicle(this, this.lineRoute.getRoute().get(0), 0, this.mainMap, mainPubTrans, null);
        int vehicleCounter = 0;
        int tickCounter = 0;
        while(tmpVehicle.getStartPosition() != this.lineRoute.getRoute().size() - 1){
            if(tmpVehicle.getStartPosition() == vehicleCounter){
                if(this.lineRoute.getRouteType().get(vehicleCounter).equals("stop")){
                    vehicleCounter ++;
                    timesForward.add(tickCounter);
                } else {
                    vehicleCounter ++;
                }
            }
            tickCounter ++;
            tmpVehicle.ride();
        }
        timesForward.add(tickCounter);
        tmpVehicle = new Vehicle(this, this.lineRoute.getRoute().get(this.lineRoute.getRoute().size() - 1), 0, this.mainMap, mainPubTrans, null);
        tmpVehicle.setForward(false);
        vehicleCounter = this.lineRoute.getRoute().size() - 1;
        tickCounter = 0;
        while(tmpVehicle.getStartPosition() != 0){
            if(tmpVehicle.getStartPosition() == vehicleCounter){
                if(this.lineRoute.getRouteType().get(vehicleCounter).equals("stop")){
                    vehicleCounter --;
                    timesBackward.add(tickCounter);
                } else {
                    vehicleCounter --;
                }
            }
            tickCounter ++;
            tmpVehicle.ride();
        }
        timesBackward.add(tickCounter);
        // this will generate warning in java 11 but in java 8 it should be fine
        BufferedImage saveImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D canvas = saveImage.createGraphics();
        canvas.setColor(java.awt.Color.white);
        canvas.fillRect(0, 0, width, height);
        java.awt.Color lineColor = new java.awt.Color((float) this.lineColor.getRed(),
                (float) this.lineColor.getGreen(),
                (float) this.lineColor.getBlue(),
                (float) this.lineColor.getOpacity());

        canvas.setColor(lineColor);
        canvas.fillRect(0,0,50, 50);
        canvas.setColor(java.awt.Color.black);
        canvas.drawString("Line ".concat(String.valueOf(this.lineNumber).concat(" connections")), 70, 30);
        //canvas.fillRect(0, 50, 50, 50);
        int counter = 0;
        for(int i = 0; i < this.lineRoute.getRoute().size(); i++){
            if(this.lineRoute.getRouteType().get(i).equals("stop")){
                canvas.setColor(java.awt.Color.yellow);
                canvas.fillRect(counter * 120 + horizontalShift, 80, 10, 10);
                stopX.add(counter * 120 + horizontalShift);
                canvas.setColor(java.awt.Color.black);
                canvas.drawString(this.mainMap.getStopByCoord(this.lineRoute.getRoute().get(i)).getName(), counter * 120 + (horizontalShift - 25), 70);
                if(counter > 0){
                    canvas.drawLine((counter - 1) * 120 + horizontalShift + 10, 85, counter * 120 + horizontalShift, 85);
                }
                counter ++;
            }
        }
        counter = 0;
        Timer tmpTimer = new Timer();
        while(!(tmpTimer.getHours() == 23 && tmpTimer.getMinutes() == 59)){
            for(int i = 0; i < this.scheduledConnections.size(); i++){
                if(tmpTimer.getHours() == this.scheduledConnections.get(i).getDepartureTime().getHours() &&
                    tmpTimer.getMinutes() == this.scheduledConnections.get(i).getDepartureTime().getMinutes()){
                    canvas.setColor(java.awt.Color.black);
                    Timer tmpDeparture = new Timer();
                    if(this.scheduledConnections.get(i).getVehicleForward()){
                        canvas.drawString("Connection ".concat(String.valueOf(i)).concat(" >"), 2, 125 + counter * 50);
                        for(int j = 0; j < timesForward.size(); j++){
                            tmpDeparture.set(this.scheduledConnections.get(i).getDepartureTime().getSeconds(),
                                    this.scheduledConnections.get(i).getDepartureTime().getMinutes(),
                                    this.scheduledConnections.get(i).getDepartureTime().getHours());
                            tmpDeparture.addSeconds(timesForward.get(j) * mainPubTrans.getTickMeansSec());
                            if(tmpDeparture.getMinutes() < 10){
                                canvas.drawString(String.valueOf(tmpDeparture.getHours()).concat(":0")
                                        .concat(String.valueOf(tmpDeparture.getMinutes())), stopX.get(j)- 10, 125 + counter * 50);
                            } else {
                                canvas.drawString(String.valueOf(tmpDeparture.getHours()).concat(":")
                                        .concat(String.valueOf(tmpDeparture.getMinutes())), stopX.get(j) - 10, 125 + counter * 50);
                            }
                        }
                    } else {
                        canvas.drawString("Connection ".concat(String.valueOf(i)).concat(" <"), 2, 125 + counter * 50);
                        for(int j = stopX.size() - 1; j >= 0; j--){
                            tmpDeparture.set(this.scheduledConnections.get(i).getDepartureTime().getSeconds(),
                                    this.scheduledConnections.get(i).getDepartureTime().getMinutes(),
                                    this.scheduledConnections.get(i).getDepartureTime().getHours());
                            tmpDeparture.addSeconds(timesBackward.get(timesBackward.size() - j - 1) * mainPubTrans.getTickMeansSec());
                            if(tmpDeparture.getMinutes() < 10){
                                canvas.drawString(String.valueOf(tmpDeparture.getHours()).concat(":0")
                                        .concat(String.valueOf(tmpDeparture.getMinutes())), stopX.get(j) - 10, 125 + counter * 50);
                            } else {
                                canvas.drawString(String.valueOf(tmpDeparture.getHours()).concat(":")
                                        .concat(String.valueOf(tmpDeparture.getMinutes())), stopX.get(j) - 10, 125 + counter * 50);
                            }
                        }
                    }
                    counter ++;
                }
            }
            tmpTimer.addMinute();
        }
        canvas.dispose();
        try{
            File saveFile = new File(filepath.concat("/scheduleLine").concat(String.valueOf(this.lineNumber).concat(".png")));
            ImageIO.write(saveImage, "png", saveFile);
        } catch (IOException e){
            System.out.println("File cannot be saved");
        }
    }
}