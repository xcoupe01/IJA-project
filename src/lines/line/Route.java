package lines.line;

import javafx.scene.layout.Pane;
import lines.Iline.iRoute;
import map.maps.Coordinate;
import javafx.scene.shape.Line;
import map.maps.Map;

/**
 * Route class
 * implements interface iRoute
 * Used to represent lines in public transport
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Route implements iRoute {

    /** Is the coordinates of the route points*/
    private java.util.List<Coordinate> coords = new java.util.ArrayList<>();
    /** Is the types of the coordinates form "coords", "point" means regular coordinate "stop" means stop*/
    private java.util.List<java.lang.String> type = new java.util.ArrayList<>();
    /** Is the graphical highlight of the line*/
    private java.util.List<Line> lineHighlight = new java.util.ArrayList<>();
    /** Is the line to which the route belongs to*/
    private PTLine line;
    /** Tells if the route highlight is visible*/
    private boolean drawn = false;
    /** Is the connection to map*/
    private Map map;

    /**
     * Native constructor of Route class
     * @param map is the map connection
     * @param line is the line connection
     */
    Route(Map map, PTLine line){
        this.line = line;
        this.map = map;
        line.setRoute(this);
    }

    /**
     * Tells if a given coordinate can be added to route. The condition is that the point must be in the map,
     * the first point must be a stop and the the next must be directly connected with street.
     * @param c is the point to be checked
     * @param type type of the point, "point" if regular coordinate "stop" if stop
     * @return true if it matches the condition false otherwise
     */
    public boolean canAdd(Coordinate c, java.lang.String type){
        if(!(this.coords.size() > 0)){
            return type.equals("stop");
        }
        for(int i = 0; i < this.map.getStreets().size(); i++){
            for(int j = 0; j < this.map.getStreets().get(i).getStreetRoute().size(); j++){
                if(this.map.getStreets().get(i).getStreetRoute().get(j).equals(this.coords.get(this.coords.size() - 1)) &&
                        this.map.getStreets().get(i).getStreetRouteType().get(j).equals(this.type.get(this.type.size() - 1))){
                    for(int k = j - 1; k >= 0 ; k--){
                        if(this.map.getStreets().get(i).getStreetRouteType().get(k).equals("stop")){
                            if(type.equals("stop") && this.map.getStreets().get(i).getStreetRoute().get(k).equals(c)){
                                return true;
                            }
                        } else {
                            if(type.equals("point") && this.map.getStreets().get(i).getStreetRoute().get(k).equals(c)){
                                return true;
                            } else {
                                for(int l = j + 1; l < this.map.getStreets().get(i).getStreetRoute().size(); l++){
                                    if(this.map.getStreets().get(i).getStreetRouteType().get(l).equals("stop")){
                                        if(type.equals("stop") && this.map.getStreets().get(i).getStreetRoute().get(l).equals(c)){
                                            return true;
                                        }
                                    } else {
                                        if(type.equals("point") && this.map.getStreets().get(i).getStreetRoute().get(l).equals(c)){
                                            return true;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Adds given stop to route if its possible
     * @param c is the coordinate of the stop we want to add
     * @return true if the operation was successful false otherwise
     */
    public boolean addStop(Coordinate c){
        if(canAdd(c, "stop")){
            this.coords.add(map.getCoord(c));
            this.type.add("stop");
            if(this.coords.size() > 1){
                Line newHighlightLine = new Line(this.coords.get(this.coords.size() - 2).getX(), this.coords.get(this.coords.size() - 2).getY(),
                        this.coords.get(this.coords.size() - 1).getX(), this.coords.get(this.coords.size() - 1).getY());
                newHighlightLine.setStrokeWidth(3);
                newHighlightLine.setStroke(this.line.getLineColor());
                this.lineHighlight.add(newHighlightLine);
            }
            return true;
        }
        return false;
    }

    /**
     * Adds given point to route if its possible
     * @param c is the coordinate we want to add
     * @return true if the operation was successful
     */
    public boolean addPoint(Coordinate c){
        if(canAdd(c, "point")){
            this.coords.add(map.getCoord(c));
            this.type.add("point");
            if(this.coords.size() > 1){
                Line newHighlightLine = new Line(this.coords.get(this.coords.size() - 2).getX(), this.coords.get(this.coords.size() - 2).getY(),
                        this.coords.get(this.coords.size() - 1).getX(), this.coords.get(this.coords.size() - 1).getY());
                newHighlightLine.setStrokeWidth(3);
                newHighlightLine.setStroke(this.line.getLineColor());
                this.lineHighlight.add(newHighlightLine);
            }
            return true;
        }
        return false;
    }

    /**
     * Updates route highlight on given Pane
     * @param mapCanvas is the Pane where the highlight is going to be updated
     */
    public void updateRouteHighlight(Pane mapCanvas){
        boolean tmp = drawn;
        this.erase(mapCanvas);
        for(int i = 0; i < this.lineHighlight.size(); i++){
            Line newHighlightLine = new Line(this.coords.get(i).getX(), this.coords.get(i).getY(),
                    this.coords.get(i + 1).getX(), this.coords.get(i + 1).getY());
            newHighlightLine.setStrokeWidth(3);
            newHighlightLine.setStroke(this.line.getLineColor());
            this.lineHighlight.set(i, newHighlightLine);
        }
        if(tmp){
            this.draw(mapCanvas);
        }
    }

    /**
     * Tells if the route is complete which means if the first and last points are stops
     * @return true if the route is complete
     */
    public boolean completeRoute(){ return this.type.get(0).equals("stop") && this.type.get(this.type.size() - 1).equals("stop"); }

    /**
     * Returns the list of coordinates that describes the route
     * @return list of coordinates that describes the route
     */
    public java.util.List<Coordinate> getRoute(){ return this.coords; }

    /**
     * Returns the list of coordinate types that describes the route
     * @return list of coordinate types that describes the route
     */
    public java.util.List<java.lang.String> getRouteType(){ return this.type; }

    public void draw(Pane mapCanvas){
        if(!this.drawn){
            this.drawn = true;
            for (Line line : this.lineHighlight) {
                mapCanvas.getChildren().add(line);
            }
        }
    }

    /**
     * Erase the route highlight from a given Pane
     * @param mapCanvas the Pane where the highlight route is going to be erased
     */
    public void erase(Pane mapCanvas){
        if(this.drawn){
            this.drawn = false;
            for (Line line : this.lineHighlight) {
                mapCanvas.getChildren().remove(line);
            }
        }
    }

    /**
     * Removes last point that defines route
     * @param mapCanvas is the Pane where the route is drawn
     */
    public void removeLast(Pane mapCanvas){
        if(this.coords.size() > 1){
            this.coords.remove(this.coords.size() - 1);
            this.type.remove(this.type.size() - 1);
            mapCanvas.getChildren().remove(this.lineHighlight.get(this.lineHighlight.size() - 1));
            this.lineHighlight.remove(this.lineHighlight.size() - 1);
        }
    }
}