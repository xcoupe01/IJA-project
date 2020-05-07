package lines.line;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.Iline.iPublicTransport;
import map.maps.Coordinate;
import map.maps.Map;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PublicTransport class
 * implements interface iPublicTransport
 * Used to represent public transport
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class PublicTransport implements iPublicTransport {

    /** List of lines*/
    private java.util.List<PTLine> lines = new java.util.ArrayList<>();
    /** Main aplication timer*/
    private Timer mainTimer = new Timer();
    /** Tells if the public transport highlight is drawn*/
    private boolean highlightDrawn = false;
    /** Connection to time display label*/
    private Label timeDisplay = new Label();
    /** Animator thread class*/
    private PTAnimator mainAnimator = new PTAnimator(this);
    /** Tells how many seconds is added every step*/
    private int tickMeansSec = 10;
    /** Tells how many steps should vehicles wait at stop*/
    private int ticksAtStop = 20;
    /** Connection to map*/
    private Map mainMap;
    /** Tells if the animator is stopped*/
    private boolean stopAnimator = true;
    /** Tells the delay between steps in milliseconds*/
    private int animationStepDelay = 100;
    /** Tells if the animator is playing*/
    private boolean animatorPlaying = false;

    /**
     * Native constructor of PublicTransport class
     * @param mainMap is the connection to map
     */
    public PublicTransport(Map mainMap){
        this.mainMap = mainMap;
    }

    /**
     * Adds line to public transport
     * @param line is the line to be added
     */
    public void addLine(PTLine line){ this.lines.add(line); }

    /**
     * Returns the list of lines of the public transport
     * @return list of lines of public transport
     */
    public java.util.List<PTLine> getLines(){ return this.lines; }

    /**
     * Moves all the objects of public transport
     * @param mapCanvas is the Pane where the public transport is being moved
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    public void updatePTPos(Pane mapCanvas, int x, int y){
        for (PTLine line : this.lines) {
            line.getRoute().updateRouteHighlight(mapCanvas);
            line.updateVehicles(x, y);
        }
    }

    /**
     * Loads the public transport form given file and draws all the vehicles on the given Pane
     * @param mapCanvas is the Pane where the vehicles is going to be drawn
     * @param filePath is the path to file
     * @param mainMap is connection to map
     * @param lineInformationPane is connection to information Pane
     * @return true if successful false otherwise
     */
    public boolean loadPTFromFile(Pane mapCanvas,String filePath, Map mainMap, Pane lineInformationPane){
        try{
            Pattern lineCoordinate = Pattern.compile("\\[(\\w+),(\\d+)]");
            Pattern properties = Pattern.compile("^LINE (\\d+) (\\w+)");
            Pattern vehicle = Pattern.compile("^VEHICLE (\\d+) (\\w+) (\\d+)$");
            File myFile = new File(filePath);
            Scanner myReader = new Scanner(myFile);
            this.eraseAllVehicles(mapCanvas);
            this.highlightAllRoutesOff(mapCanvas);
            this.lines.clear();
            while(myReader.hasNextLine()){
                String line = myReader.nextLine();
                if(line.matches("^LINE \\d+ \\w+ [\\[\\w+,\\d+\\]\\s*]+$")){
                    Matcher matchedProperties = properties.matcher(line);
                    Matcher matchedLineCoords = lineCoordinate.matcher(line);
                    if(matchedLineCoords.find() && matchedProperties.find()){
                        PTLine tmpPTLine = new PTLine(Integer.parseInt(matchedProperties.group(1)), Color.valueOf(matchedProperties.group(2)), mainMap);
                        Route tmpRoute = new Route(mainMap, tmpPTLine);
                        Coordinate tmpCoord = mainMap.getStreets().get(mainMap.getMapPointerById(matchedLineCoords.group(1))).getStreetRoute().get(Integer.parseInt(matchedLineCoords.group(2)));
                        if(!mainMap.getStreets().get(mainMap.getMapPointerById(matchedLineCoords.group(1))).getStreetRouteType().get(Integer.parseInt(matchedLineCoords.group(2))).equals("stop")){
                            return false;
                        } else {
                            tmpRoute.addStop(tmpCoord);
                        }
                        while(matchedLineCoords.find()){
                            tmpCoord = mainMap.getStreets().get(mainMap.getMapPointerById(matchedLineCoords.group(1))).getStreetRoute().get(Integer.parseInt(matchedLineCoords.group(2)));
                            if(mainMap.getStreets().get(mainMap.getMapPointerById(matchedLineCoords.group(1))).getStreetRouteType().get(Integer.parseInt(matchedLineCoords.group(2))).equals("stop")){
                                tmpRoute.addStop(tmpCoord);
                            } else if(mainMap.getStreets().get(mainMap.getMapPointerById(matchedLineCoords.group(1))).getStreetRouteType().get(Integer.parseInt(matchedLineCoords.group(2))).equals("point")){
                                tmpRoute.addPoint(tmpCoord);
                            } else {
                                return false;
                            }
                        }
                        this.addLine(tmpPTLine);
                    } else {
                        return false;
                    }
                } else if(line.matches("^VEHICLE \\d+ \\w+ \\d+$")){
                    Matcher matchedVehicles = vehicle.matcher(line);
                    if(matchedVehicles.find() && this.lines.size() > 0 && this.lines.get(this.lines.size() - 1).getRoute().getRoute().size() > Integer.parseInt(matchedVehicles.group(1))){
                        this.lines.get(this.lines.size() - 1).addVehicle(Integer.parseInt(matchedVehicles.group(1)), this);
                        this.lines.get(this.lines.size() - 1).getVehicles().get(this.lines.get(this.lines.size() - 1).getVehicles().size() - 1).setInformationPane(lineInformationPane);
                        if(matchedVehicles.group(2).equals("forward")){
                            this.lines.get(this.lines.size() - 1).getVehicles().get(this.lines.get(this.lines.size() - 1).getVehicles().size() - 1).setForward(true);
                        } else if(matchedVehicles.group(2).equals("backward")){
                            this.lines.get(this.lines.size() - 1).getVehicles().get(this.lines.get(this.lines.size() - 1).getVehicles().size() - 1).setForward(false);
                        }
                        for(int i = 0; i <= Integer.parseInt(matchedVehicles.group(3)); i++){
                            this.lines.get(this.lines.size() - 1).getVehicles().get(this.lines.get(this.lines.size() - 1).getVehicles().size() - 1).ride();
                        }
                    } else {
                        return false;
                    }
                } else if(!line.matches("")){
                    return false;
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
    this.drawAllVehicles(mapCanvas);
    return true;
    }

    /**
     * Saves curresnt publci transport to a given file path
     * @param filePath is the file to be saved to
     * @param mainMap is the connection to map
     */
    public void savePTToFile(String filePath, Map mainMap){
        try{
            StringBuilder toSave = new StringBuilder();
            FileWriter savefile = new FileWriter(filePath, false);
            BufferedWriter file = new BufferedWriter(savefile);
            for (PTLine line : this.lines) {
                toSave.append("LINE ").append(line.getLineNumber()).append(" ").append(line.getLineColor());
                boolean appended = false;
                for (int j = 0; j < line.getRoute().getRoute().size(); j++) {
                    for(int k = 0; k < mainMap.getStreets().size(); k++){
                        for(int l = 0; l < mainMap.getStreets().get(k).getStreetRoute().size(); l++){
                            if(mainMap.getStreets().get(k).getStreetRoute().get(l).equals(line.getRoute().getRoute().get(j)) && !appended){
                                toSave.append(" [").append(mainMap.getStreets().get(k).getId()).append(",").append(l).append("]");
                                appended = true;
                                break;
                            }
                        }
                    }
                    appended = false;
                }
                toSave.append("\n");
                for (int i = 0; i < line.getVehicles().size(); i++){
                    toSave.append("VEHICLE ").append(line.getVehicles().get(i).getStartPosition());
                    if(line.getVehicles().get(i).getForward()){
                        toSave.append(" forward ");
                    } else {
                        toSave.append(" backward ");
                    }
                    toSave.append(line.getVehicles().get(i).getTurns()).append("\n");
                }
            }
            file.write(String.valueOf(toSave));
            file.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Draws all line route highlights on a given Pane
     * @param mapCanvas is the Pane where the highlights are going to be drawn
     */
    public void highlightAllRoutesOn(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.getRoute().draw(mapCanvas);
        }
    }

    /**
     * Erases all line route highlights on a given Pane
     * @param mapCanvas is the Pane where the highlights are going to be erased
     */
    public void highlightAllRoutesOff(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.getRoute().erase(mapCanvas);
        }
    }

    /**
     * Toggles ale line route highlights on a given Pane
     * @param mapCanvas is the Pane where the line route highlights are going to be toggled
     */
    public void toggleHighlight(Pane mapCanvas){
        if(highlightDrawn){
            highlightDrawn = false;
            this.highlightAllRoutesOff(mapCanvas);
        } else {
            highlightDrawn = true;
            this.highlightAllRoutesOn(mapCanvas);
        }
    }

    /**
     * Draws all vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be drawn
     */
    public void drawAllVehicles(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.drawVehicles(mapCanvas);
        }
    }

    /**
     * Erases all vehicles on a given Pane
     * @param mapCanvas is the Pane where the vehicles are going to be erased
     */
    public void eraseAllVehicles(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.eraseVehicles(mapCanvas);
        }
    }

    /**
     * Makes one animation step
     */
    public void animationStep(){
        this.mainTimer.addSeconds(this.tickMeansSec);
        this.updateTimeDisplay();
        for (PTLine line : this.lines) {
            line.rideAllVehicles();
        }
        if(this.mainMap.getStopThatOccupiesInformationPane() != null){
            this.mainMap.getStopThatOccupiesInformationPane().drawInformation();
        }
    }

    /**
     * Attaches connection to time display label
     * @param timeDisplay is the label to be attached
     */
    public void setTimeDisplay(Label timeDisplay){ this.timeDisplay = timeDisplay; }

    /**
     * Updates time display label with values from timer
     */
    public void updateTimeDisplay(){
        if(this.mainTimer.getMinutes() < 10){
            this.timeDisplay.setText("Time : ".concat(String.valueOf(this.mainTimer.getHours()).concat(":0").concat(String.valueOf(this.mainTimer.getMinutes()))));
        } else {
            this.timeDisplay.setText("Time : ".concat(String.valueOf(this.mainTimer.getHours()).concat(":").concat(String.valueOf(this.mainTimer.getMinutes()))));
        }
    }

    /**
     * Returns timer of public transport
     * @return timer of public transport
     */
    public Timer getTimer(){ return this.mainTimer; }

    /**
     * Sets how many seconds means one animation step
     * @param num the number in seconds the animation step takes in application timer
     */
    public void setTickMeansSec(int num){
        this.tickMeansSec = num;
        for (PTLine line : this.lines) {
            line.setVehiclesTickMeansSec(num);
        }
    }

    /**
     * Sets how many animation steps will the vehicles wait at stops
     * @param num the amount of animation steps vehicles will wait at stops
     */
    public void setTicksAtStop(int num){
        this.ticksAtStop = num;
        for (PTLine line : this.lines) {
            line.setVehiclesTicksAtStop(num);
        }
    }

    /**
     * Sets all vehicles information Pane occupy value to false
     */
    public void setAllVehiclesInformationPaneOccupyFalse(){
        for (PTLine line : this.lines) {
            for (int j = 0; j < line.getVehicles().size(); j++) {
                line.getVehicles().get(j).setInformationPaneOccupyFalse();
            }
        }
    }

    /**
     * Tells which vehicle currently occupies the information Pane
     * @return vehicle that occupies the information pane, null if none
     */
    public Vehicle getVehicleOccupyInformationPaneTrue(){
        for (PTLine line : this.lines) {
            for (int j = 0; j < line.getVehicles().size(); j++) {
                if (line.getVehicles().get(j).getInformationPaneOccupy()) {
                    return line.getVehicles().get(j);
                }
            }
        }
        return null;
    }

    /**
     * Tells if the animations are stopped
     * @return true if the animations are stopped
     */
    public boolean getStopAnimator(){ return this.stopAnimator; }

    /**
     * Sets animation stop value
     * @param stopAnimator true means stop false means run
     */
    public void setStopAnimator(boolean stopAnimator){
        this.stopAnimator = stopAnimator;
        this.animatorPlaying = false;
        if(!stopAnimator){
            this.mainAnimator.resetThread();
        }
    }

    /**
     * Starts the animator thread and the animations
     */
    public void playAnimator(){
        if(!this.animatorPlaying){
            this.mainAnimator.start();
            this.animatorPlaying = true;
        }
    }

    /**
     * Sets the animation step delay
     * @param num is the animation step delay in milliseconds
     */
    public void setAnimationStepDelay(int num){ this.animationStepDelay = num; }

    /**
     * Tells whats current animation step delay
     * @return animation step delay in milliseconds
     */
    public int getAnimationStepDelay(){ return this.animationStepDelay; }

    /**
     * Tells how manny seconds means one animation step in application time
     * @return number that represents how manny seconds means one animation step in application time
     */
    public int getTickMeansSec(){ return this.tickMeansSec; }

    //TODO generate daily timetable
}