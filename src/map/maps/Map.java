package map.maps;


import javafx.scene.layout.Pane;
import map.Imaps.iMap;

import java.io.File;
import java.io.FileNotFoundException;
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

    public boolean loadMapFromFile(java.lang.String filePath){
        try {
            Pattern coordinate = Pattern.compile("\\[(\\d+),(\\d+)]");
            Pattern name = Pattern.compile("^\\w+ (\\w+)");
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                System.out.println(line);
                if(line.matches("^STREET \\w+ [\\[\\d+,\\d+\\]]+")){
                    Matcher matchedCoords = coordinate.matcher(line);
                    Matcher matchedName = name.matcher(line);
                    this.addStreet(new Street(matchedName.group(0),
                            Coordinate.create( Integer.parseInt(matchedCoords.group(0)), Integer.parseInt(matchedCoords.group(1)) )));
                    for(int i = 2; i < matchedCoords.groupCount(); i++){
                        this.streets.get(this.streets.size() - 1).addCoord( Coordinate.create( Integer.parseInt(matchedCoords.group(i)), Integer.parseInt(matchedCoords.group(i+1)) ) );
                        i++;
                    }
                } else if(line.matches("^STOP \\w+ \\[\\d+,\\d+]$")){
                    Matcher matchedCoords = coordinate.matcher(line);
                    Matcher matchedName = name.matcher(line);
                    if(this.streets.size() > 0){
                        this.streets.get(this.streets.size() - 1).addStop(new Stop(matchedName.group(0),
                                Objects.requireNonNull(Coordinate.create(Integer.parseInt(matchedCoords.group(0)), Integer.parseInt(matchedCoords.group(1))))));
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

    }
    // TODO make loader and saver from/to file

}