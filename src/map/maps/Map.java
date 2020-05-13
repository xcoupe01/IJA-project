package map.maps;

import javafx.scene.layout.Pane;
import lines.line.PublicTransport;
import map.Imaps.iMap;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Map class
 * implements interface iMap
 * Used to represent map
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Map implements iMap{

    /**Array of all streets in map*/
    private java.util.List<Street> streets = new java.util.ArrayList<>();
    /**Connection to public transport class*/
    private PublicTransport mainPubTrans;
    /**Connection to information Pane*/
    private Pane informationPane;

    /**
     * Adds given street to map
     * @param street is street that's going to be added to map
     */
    public void addStreet(Street street){ this.streets.add(street); }

    /**
     * Returns all streets saved in map
     * @return list of streets
     */
    public java.util.List<Street> getStreets(){ return this.streets; }

    /**
     * Makes the whole map visible on given Pane
     * @param mapCanvas the Pane that the map is going to be drawn to
     */
    public void draw(Pane mapCanvas){
        for (Street street : streets) {
            street.draw(mapCanvas);
        }
    }

    /**
     * Moves all objects in map
     * @param mapCanvas is Pane where the map is drawn
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
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

    /**
     * Returns list index (pointer) into street list in this map for a given street, if not found 0 returned
     * @param id is the id (probably name) of street
     * @return list index into street list to street that matches given id (name)
     */
    public int getMapPointerById(java.lang.String id){
        for(int i = 0; i < this.streets.size(); i++){
            if(this.streets.get(i).getId().equals(id)){
                return i;
            }
        }
        return 0;
    }

    /**
     * Tells if given id corresponds to some id (name) of some street in street list
     * @param id is the id (name) to be inspected
     * @return true if the id (name) is found false otherwise
     */
    public boolean isStreet(java.lang.String id){
        for (Street street : this.streets) {
            if (street.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Turns all street highlights of on given Pane
     * @param mapCanvas is the Pane where the highlight is going to be turned off
     */
    public void highlightOffAll(Pane mapCanvas){
        for (Street street : this.streets) {
            street.highlightOff(mapCanvas);
        }
    }

    /**
     * Loads map from a specified file and draws it on given Pane
     * @param filePath is the path to the file (.map file recommended)
     * @param mapCanvas is the Pane where the map is going to be displayed
     * @return true if successful false otherwise
     */
    public boolean loadMapFromFile(java.lang.String filePath, Pane mapCanvas){
        try {
            Pattern coordinate = Pattern.compile("\\[(\\d+),(\\d+)]");
            Pattern name = Pattern.compile("^\\w+ (\\w+)");
            Pattern traffic = Pattern.compile("(\\d+)");
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            this.streets.clear();
            mapCanvas.getChildren().clear();
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if(line.matches("^STREET \\w+ [\\[\\d+,\\] ]+")){
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
                                    Objects.requireNonNull(Coordinate.create(Integer.parseInt(matchedCoords.group(1))
                                            , Integer.parseInt(matchedCoords.group(2)))),this.mainPubTrans, this.informationPane, this))){
                                System.out.println("WARNING - stop ".concat(matchedName.group(1)).concat(" cannot be added because of position"));
                            }
                        }
                    } else {
                        return false;
                    }
                } else if(line.matches("^TRAFFIC [\\d+ ]+")){
                    Matcher matchedTraffic = traffic.matcher(line);
                    if(this.streets.size() > 0){
                            for(int i = 0; matchedTraffic.find(); i++){
                                this.streets.get(this.streets.size() - 1).setTraffic(i, Integer.parseInt(matchedTraffic.group(1)));
                            }
                    } else {
                        return false;
                    }
                } else if(!line.matches("") && !line.matches("^#")){
                    System.out.println("unknown command - ".concat(line));
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

    /**
     * Generates map save file to a given path
     * @param filePath is the file to which the map is going to be saved
     */
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
                toSave.append("\n");
                toSave.append("TRAFFIC");
                for(int i = 0; i < street.getTraffic().size(); i++){
                    toSave.append(" ".concat(String.valueOf(street.getTraffic().get(i))));
                }
                toSave.append("\n");
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

    /**
     * Tells if given coordinate is on the map
     * @param c is the coordinate to be looked for
     * @return true if the coordinate is on the map false otherwise
     */
    public boolean isCoordOnMap(Coordinate c){
        for (Street street : this.streets) {
            for (int j = 0; j < street.getCoordinates().size(); j++) {
                if (street.getCoordinates().get(j).equals(c)) {
                    return true;
                }
            }
            for (int j = 0; j < street.getStops().size(); j++) {
                if (street.getStops().get(j).getCoord().equals(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns coordinate object from map with same position as given coordinate
     * @param c is the coordinate to be found
     * @return coordinate with same position from map
     */
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

    /**
     * Returns stop object from map with the same position as given coordinate
     * @param c is the coordinate to be found
     * @return stop with the same position from map
     */
    public Stop getStopByCoord(Coordinate c){
        for (Street street : this.streets) {
            for (int j = 0; j < street.getStops().size(); j++) {
                if (street.getStops().get(j).getCoord().equals(c)) {
                    return street.getStops().get(j);
                }
            }
        }
        return null;
    }

    /**
     * Attaches public transport connection to map
     * @param mainPubTrans is the public transport to be added
     */
    public void attachPubTrans(PublicTransport mainPubTrans){ this.mainPubTrans = mainPubTrans; }

    /**
     * Attaches information Pane connection to map
     * @param informationPane is the Pane to be attached
     */
    public void attachInformationPane(Pane informationPane){ this.informationPane = informationPane; }

    /**
     * Sets all stops occupation property to false
     */
    public void setAllStopInformationPaneOccupyFalse(){
        for (Street street : this.streets) {
            for (int j = 0; j < street.getStops().size(); j++) {
                street.getStops().get(j).InformationPaneOccupyFalse();
            }
        }
    }

    /**
     * Returns the stop that haves occupation property set to true
     * @return stop with occupation property equals to true
     */
    public Stop getStopThatOccupiesInformationPane(){
        for (Street street : this.streets) {
            for (int j = 0; j < street.getStops().size(); j++) {
                if (street.getStops().get(j).getInformationPaneOccupy()) {
                    return street.getStops().get(j);
                }
            }
        }
        return null;
    }

    /**
     * Propagation of street class function getTrafficAt. Returns the first matched traffic by given coordinates.
     * When traffic not found, it returns basic value 1 and writes an error note to console.
     * @param c1 one of coordinates to be searched for
     * @param c2 one of coordinates to be searched for
     * @return level of traffic
     */
    public int getTrafficLevelByCoords(Coordinate c1, Coordinate c2){
        for (Street street : this.streets) {
            int tmpTraffic = street.getTrafficAt(c1, c2);
            if (tmpTraffic != -1) {
                return tmpTraffic;
            }
        }
        System.out.println("Map.getTrafficLevelByCoords - function failed and returned basic value");
        return 1;
    }
}