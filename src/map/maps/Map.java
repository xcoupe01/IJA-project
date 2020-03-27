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
    // TODO make loader from file

}