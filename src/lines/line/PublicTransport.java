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

public class PublicTransport implements iPublicTransport {
    private java.util.List<PTLine> lines = new java.util.ArrayList<>();
    private Timer mainTimer = new Timer();
    private boolean highlightDrawn = false;
    private Label timeDisplay = new Label();
    private PTAnimator mainAnimator = new PTAnimator(this);
    private int tickMeansSec = 10;
    private int ticksAtStop = 20;
    private Map mainMap;
    private boolean stopAnimator = true;

    public PublicTransport(Map mainMap){
        this.mainMap = mainMap;
    }

    public void addLine(PTLine line){ this.lines.add(line); }
    public java.util.List<PTLine> getLines(){ return this.lines; }
    public void updatePTPos(Pane mapCanvas, int x, int y){
        for (PTLine line : this.lines) {
            line.getRoute().updateRouteHighlight(mapCanvas);
            line.updateVehicles(x, y);
        }
    }
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

    public void highlightAllRoutesOn(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.getRoute().draw(mapCanvas);
        }
    }
    public void highlightAllRoutesOff(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.getRoute().erase(mapCanvas);
        }
    }

    public void eraseAllVehicles(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.eraseVehicles(mapCanvas);
        }
    }

    public void drawAllVehicles(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.drawVehicles(mapCanvas);
        }
    }

    public void toggleHighlight(Pane mapCanvas){
        if(highlightDrawn){
            highlightDrawn = false;
            this.highlightAllRoutesOff(mapCanvas);
        } else {
            highlightDrawn = true;
            this.highlightAllRoutesOn(mapCanvas);
        }
    }

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

    public void setTimeDisplay(Label timeDisplay){ this.timeDisplay = timeDisplay; }

    public void updateTimeDisplay(){
        if(this.mainTimer.getMinutes() < 10){
            this.timeDisplay.setText("Time : ".concat(String.valueOf(this.mainTimer.getHours()).concat(":0").concat(String.valueOf(this.mainTimer.getMinutes()))));
        } else {
            this.timeDisplay.setText("Time : ".concat(String.valueOf(this.mainTimer.getHours()).concat(":").concat(String.valueOf(this.mainTimer.getMinutes()))));
        }
    }

    public Timer getTimer(){ return this.mainTimer; }

    public void setTickMeansSec(int num){
        this.tickMeansSec = num;
        for (PTLine line : this.lines) {
            line.setVehiclesTickMeansSec(num);
        }
    }

    public void setTicksAtStop(int num){
        this.ticksAtStop = num;
        for (PTLine line : this.lines) {
            line.setVehiclesTicksAtStop(num);
        }
    }

    public void setAllVehiclesInformationPaneOccupyFalse(){
        for (PTLine line : this.lines) {
            for (int j = 0; j < line.getVehicles().size(); j++) {
                line.getVehicles().get(j).setInformationPaneOccupyFalse();
            }
        }
    }

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

    public boolean getStopAnimator(){ return this.stopAnimator; }
    public void setStopAnimator(boolean stopAnimator){
        this.stopAnimator = stopAnimator;
        if(!stopAnimator){
            this.mainAnimator.resetThread();
        }
    }
    public void playAnimator(){ this.mainAnimator.start();}

    //TODO generate daily timetable
}