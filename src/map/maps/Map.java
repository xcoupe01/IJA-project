package map.maps;


import javafx.scene.layout.Pane;
import map.Imaps.iMap;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Map implements iMap{

    private java.util.List<Street> streets = new java.util.ArrayList<>();   //< array of all streets in map

    // adds given street to map
    public void addStreet(Street street){ this.streets.add(street); }

    // returns all streets saved in map
    public java.util.List<Street> getStreets(){ return this.streets; }

    // makes the whole map visible on given Pane
    public void draw(Pane mapCanvas){
        for (Street street : streets) {
            street.draw(mapCanvas);
        }
    }

    public void moveMap(Pane mapCanvas, int x, int y){
        for (Street tmpStreet : this.streets) {
            for (int j = 0; j < tmpStreet.getCoordinates().size(); j++) {
                tmpStreet.getCoordinates().get(j).setMoved();
            }
        }
        for (Street street : this.streets) {
            street.moveStreet(mapCanvas, x, y);
        }
    }

    public int getMapPointerById(java.lang.String id){
        for(int i = 0; i < this.streets.size(); i++){
            if(this.streets.get(i).getId().equals(id)){
                return i;
            }
        }
        return 0;
    }

    public boolean isStreet(java.lang.String id){
        for (Street street : this.streets) {
            if (street.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void highlightOffAll(Pane mapCanvas){
        for (Street street : this.streets) {
            street.highlightOff(mapCanvas);
        }
    }

    public boolean loadMapFromFile(java.lang.String filePath, Pane mapCanvas){
        try {
            Pattern coordinate = Pattern.compile("\\[(\\d+),(\\d+)]");
            Pattern name = Pattern.compile("^\\w+ (\\w+)");
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            this.streets.clear();
            mapCanvas.getChildren().clear();
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if(line.matches("^STREET \\w+ [\\[\\d+,\\d+\\] ]+")){
                    Matcher matchedCoords = coordinate.matcher(line);
                    Matcher matchedName = name.matcher(line);
                    if(matchedName.find() && matchedCoords.find()){
                        Coordinate tmpCoord = new Coordinate( Integer.parseInt(matchedCoords.group(1)), Integer.parseInt(matchedCoords.group(2)));
                        for (Street street : this.streets) {
                            for (int i = 0; i < street.getCoordinates().size(); i++) {
                                assert tmpCoord != null;
                                if (tmpCoord.equals(street.getCoordinates().get(i))) {
                                    tmpCoord = street.getCoordinates().get(i);
                                }
                            }
                        }
                        this.addStreet(new Street(matchedName.group(1), tmpCoord));
                    }
                    while(matchedCoords.find()){
                        Coordinate tmpCoord = new Coordinate( Integer.parseInt(matchedCoords.group(1)), Integer.parseInt(matchedCoords.group(2)));
                        for (Street street : this.streets) {
                            for (int i = 0; i < street.getCoordinates().size(); i++) {
                                assert tmpCoord != null;
                                if (tmpCoord.equals(street.getCoordinates().get(i))) {
                                    tmpCoord = street.getCoordinates().get(i);
                                }
                            }
                        }
                        this.streets.get(this.streets.size() - 1).addCoord(tmpCoord);
                    }
                } else if(line.matches("^STOP \\w+ \\[\\d+,\\d+]$")){
                    Matcher matchedCoords = coordinate.matcher(line);
                    Matcher matchedName = name.matcher(line);
                    if(this.streets.size() > 0){
                        if(matchedCoords.find() && matchedName.find()){
                            if(!this.streets.get(this.streets.size() - 1).addStop(new Stop(matchedName.group(1),
                                    Objects.requireNonNull(Coordinate.create(Integer.parseInt(matchedCoords.group(1)), Integer.parseInt(matchedCoords.group(2))))))){
                                System.out.println("WARNING - stop cannot be added because of position");
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("unknown command");
                    return false;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void saveMapToFile(java.lang.String filePath){
        try{
            StringBuilder toSave = new StringBuilder();
            FileWriter savefile = new FileWriter(filePath, false);
            BufferedWriter file = new BufferedWriter(savefile);
            for (Street street : this.streets) {
                toSave.append("STREET ").append(street.getId()).append(" ");
                for (int j = 0; j < street.getCoordinates().size(); j++) {
                    toSave.append("[").append(street.getCoordinates().get(j).getX()).append(",").append(street.getCoordinates().get(j).getY()).append("] ");
                }
                toSave.append('\n');
                for (int j = 0; j < street.getStops().size(); j++) {
                    toSave.append("STOP ").append(street.getStops().get(j).getName()).append(" [").append(street.getStops().get(j).getCoord().getX()).append(",").append(street.getStops().get(j).getCoord().getY()).append("]\n");
                }
            }
            file.write(String.valueOf(toSave));
            file.close();
        }
        catch (IOException e){
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public Coordinate getCoord(Coordinate c){
        for (Street street : this.streets) {
            for (int j = 0; j < street.getCoordinates().size(); j++) {
                if (street.getCoordinates().get(j).equals(c)) {
                    return street.getCoordinates().get(j);
                }
            }
            for (int j = 0; j < street.getStops().size(); j++) {
                if (street.getStops().get(j).getCoord().equals(c)) {
                    return street.getStops().get(j).getCoord();
                }
            }
        }
        return null;
    }
}