package lines.line;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lines.Iline.iPublicTransport;
import map.maps.Coordinate;
import map.maps.Map;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicTransport implements iPublicTransport {
    private java.util.List<PTLine> lines = new java.util.ArrayList<>();
    private int timer;
    private boolean highlightDrawn = false;
    //private Thread animator = new Thread();

    public PublicTransport(){

    }

    public void addLine(PTLine line){ this.lines.add(line); }
    public java.util.List<PTLine> getLines(){ return this.lines; }
    public void setTimer(int time){ this.timer = time; }
    public void updatePTPos(Pane mapCanvas, int x, int y){
        for (PTLine line : this.lines) {
            line.getRoute().updateRouteHighlight(mapCanvas);
            line.updateVehicles(x, y);
        }
    }
    public boolean loadPTFromFile(Pane mapCanvas,String filePath, Map mainMap){
        try{
            Pattern lineCoordinate = Pattern.compile("\\[(\\w+),(\\d+)]");
            Pattern properties = Pattern.compile("^LINE (\\d+) (\\w+)");
            Pattern vehicle = Pattern.compile("^VEHICLE (\\d+) (\\w+)$");
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
                } else if(line.matches("^VEHICLE \\d+ \\w+$")){
                    Matcher matchedVehicles = vehicle.matcher(line);
                    if(matchedVehicles.find() && this.lines.size() > 0 && this.lines.get(this.lines.size() - 1).getRoute().getRoute().size() > Integer.parseInt(matchedVehicles.group(1))){
                        this.lines.get(this.lines.size() - 1).addVehicle(Integer.parseInt(matchedVehicles.group(1)));
                        if(matchedVehicles.group(2).equals("forward")){
                            this.lines.get(this.lines.size() - 1).getVehicles().get(this.lines.get(this.lines.size() - 1).getVehicles().size() - 1).setForward(true);
                        } else if(matchedVehicles.group(2).equals("backward")){
                            this.lines.get(this.lines.size() - 1).getVehicles().get(this.lines.get(this.lines.size() - 1).getVehicles().size() - 1).setForward(false);
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
                        toSave.append(" forward\n");
                    } else {
                        toSave.append(" backward\n");
                    }
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

    public void run(){
        for (PTLine line : this.lines) {
            line.rideAllVehicles();
        }

    }

    public void rideAllVehicles(Pane mapCanvas){

    }
}