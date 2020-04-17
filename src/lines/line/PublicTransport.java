package lines.line;

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
    private int timer;
    private boolean highlightDrawn = false;

    public PublicTransport(){

    }

    public void addLine(PTLine line){ this.lines.add(line); }
    public java.util.List<PTLine> getLines(){ return this.lines; }
    public void setTimer(int time){ this.timer = time; }
    public void run(Pane mapCanvas){
        while(true){
            for(int i = 0; i < this.lines.size(); i++){
                this.lines.get(i).rideVehicles(mapCanvas);
            }
        }
    }
    public void updatePTPos(Pane mapCanvas){
        for (PTLine line : this.lines) {
            line.getRoute().updateRouteHighlight(mapCanvas);
        }
    }
    public boolean loadPTFromFile(String filePath, Map mainMap){
        try{
            Pattern lineCoordinate = Pattern.compile("([S|P])\\[(\\d+),(\\d+)]");
            Pattern properties = Pattern.compile("^LINE (\\d+) (\\w+)");
            File myFile = new File(filePath);
            Scanner myReader = new Scanner(myFile);
            // turn off all displaying
            this.lines.clear();
            while(myReader.hasNextLine()){
                String line = myReader.nextLine();
                if(line.matches("^LINE \\d+ \\w+ [[S|P]\\[\\d+,\\d+\\]\\s*]+$")){
                    Matcher matchedProperties = properties.matcher(line);
                    Matcher matchedLineCoords = lineCoordinate.matcher(line);
                    if(matchedLineCoords.find() && matchedProperties.find()){
                        PTLine tmpPTLine = new PTLine(Integer.parseInt(matchedProperties.group(1)), Color.valueOf(matchedProperties.group(2)), mainMap);
                        Route tmpRoute = new Route(mainMap, tmpPTLine);
                        Coordinate tmpCoord = new Coordinate( Integer.parseInt(matchedLineCoords.group(2)), Integer.parseInt(matchedLineCoords.group(3)));
                        if(!matchedLineCoords.group(1).equals("S")){
                            return false;
                        } else {
                            tmpRoute.addStop(tmpCoord);
                        }
                        while(matchedLineCoords.find()){
                            tmpCoord.setX(Integer.parseInt(matchedLineCoords.group(2)));
                            tmpCoord.setY(Integer.parseInt(matchedLineCoords.group(3)));
                            if(matchedLineCoords.group(1).equals("S")){
                                tmpRoute.addStop(tmpCoord);
                            } else if(matchedLineCoords.group(1).equals("P")){
                                tmpRoute.addPoint(tmpCoord);
                            } else {
                                return false;
                            }
                        }
                        this.addLine(tmpPTLine);
                    } else {
                        return false;
                    }
                } else if(line.matches("^VEHICLE \\[\\d+,\\d+]$")){
                    // TODO
                    this.lines.get(this.lines.size() - 1).addVehicle(/*where to add*/);
                } else if(!line.matches("")){
                    return false;
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
    return true;
    }
    public void savePTToFile(String filePath){
        try{
            StringBuilder toSave = new StringBuilder();
            FileWriter savefile = new FileWriter(filePath, false);
            BufferedWriter file = new BufferedWriter(savefile);
            for (PTLine line : this.lines) {
                toSave.append("LINE ").append(line.getLineNumber()).append(" ").append(line.getLineColor());
                for (int j = 0; j < line.getRoute().getRoute().size(); j++) {
                    if (line.getRoute().getRouteType().get(j).equals("stop")) {
                        toSave.append(" S[").append(line.getRoute().getRoute().get(j).getX()).append(",").append(
                                line.getRoute().getRoute().get(j).getY()).append("]");
                    } else {
                        toSave.append(" P[").append(line.getRoute().getRoute().get(j).getX()).append(",").append(
                                line.getRoute().getRoute().get(j).getY()).append("]");
                    }
                }
                //TODO vehicles come here
                toSave.append("\n");
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
    public void toggleHighlight(Pane mapCanvas){
        if(highlightDrawn){
            highlightDrawn = false;
            this.highlightAllRoutesOff(mapCanvas);
        } else {
            highlightDrawn = true;
            this.highlightAllRoutesOn(mapCanvas);
        }
    }
}