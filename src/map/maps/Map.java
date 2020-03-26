package map.maps;


import javafx.scene.layout.Pane;
import map.Imaps.iMap;

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
    // TODO make loader from file

}