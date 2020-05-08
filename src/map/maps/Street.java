package map.maps;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.Imaps.iStreet;

/**
 * Street class
 * implements interface iStreet
 * Used to represent street on map
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Street implements iStreet{

    /** List of Coordinates which creates the street*/
    private java.util.List<Coordinate> coords = new java.util.ArrayList<>();
    /** List of Stops*/
    private java.util.List<Stop> stops = new java.util.ArrayList<>();
    /** List that tells which parts of Street are visible*/
    private java.util.List<Boolean> drawn = new java.util.ArrayList<>();
    /** List of graphical lines*/
    private java.util.List<Line> StreetLines = new java.util.ArrayList<>();
    /** List of highlighted lines*/
    private java.util.List<Line> StreetHighlight = new java.util.ArrayList<>();
    /** list of coordinates and stops from start to end in correct order*/
    private java.util.List<Coordinate> streetRoute = new java.util.ArrayList<>();
    /** List of coordinate types that corresponds with "streetRoute" list. If value is "point" its regular coordinate if
     * value is "stop" it represents stop on the street*/
    private java.util.List<java.lang.String> streetRouteType = new java.util.ArrayList<>();
    /** Street id (probably name)*/
    private java.lang.String id;
    /** Tells if the street is visible*/
    private boolean wholeStreetDrawn = false;
    /** Tells if the highlight is visible*/
    private boolean highlightStatus = false;
    /** Highlight of the end point of street*/
    private Circle highlightEnd = new Circle(5);

    //TODO make traffic simulation

    /**
     * Native class constructor
     * @param id is the name of the street
     * @param begin is the coordinate of start of the street
     */
    public Street(java.lang.String id, Coordinate begin){
        this.id = id;
        this.coords.add(begin);
        this.highlightEnd.setFill(Color.RED);
        this.streetRoute.add(begin);
        this.streetRouteType.add("point");
    }

    /**
     * Add stop to the street
     * @param stop is the stop to be added to the street
     * @return true if successful false otherwise
     */
    public boolean addStop(Stop stop){
        int epsilon = 1;
        for(int i = 1; i < this.coords.size(); i++){
            if(this.coords.get(i - 1).distance(this.coords.get(i)) + epsilon > (this.coords.get(i-1).distance(stop.getCoord()) + this.coords.get(i).distance(stop.getCoord()))){
                this.stops.add(stop);
                stop.setStreet(this);
                for(int j = 0; j < this.streetRoute.size(); j++){
                    if(this.streetRoute.get(j).equals(this.coords.get(i))){
                        this.streetRoute.add(j, stop.getCoord());
                        this.streetRouteType.add(j, "stop");
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the route in coordinates of the street
     * @return list of coordinates which defines the street from start to end with points and stops
     */
    public java.util.List<Coordinate> getStreetRoute(){ return this.streetRoute;}

    /**
     * Returns the route in type of the street
     * @return list of types that corresponds to coordinates form "streetRoute"
     */
    public java.util.List<java.lang.String> getStreetRouteType(){ return this.streetRouteType;}

    /**
     * Tells if coordinate is on the street but its based on the position of the given stop
     * not if the stop is assigned to this street.
     * @param stop is the stop to be tested
     * @return true if the stop is on the street
     */
    public boolean isStopOnStreet(Stop stop){
        int epsilon = 1;
        for(int i = 1; i < this.coords.size(); i++){
            if(this.coords.get(i - 1).distance(this.coords.get(i)) + epsilon > (this.coords.get(i-1).distance(stop.getCoord()) + this.coords.get(i).distance(stop.getCoord()))){
                stop.setStreet(this);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds new coordinate to define the street
     * @param coord is the coordinate that's going to be added
     */
    public void addCoord(Coordinate coord){
        this.coords.add(coord);
        this.drawn.add(false);
        this.streetRoute.add(coord);
        this.streetRouteType.add("point");
        Line line = new Line(this.coords.get(this.coords.size() - 2).getX(), this.coords.get(this.coords.size() - 2).getY(),
                coord.getX(), coord.getY());
        line.setStrokeWidth(1);
        line.setStroke(Color.BLACK.deriveColor(1, 1, 1, 0.7));
        this.StreetLines.add(line);
        Line highlight = new Line(this.coords.get(this.coords.size() - 2).getX(), this.coords.get(this.coords.size() - 2).getY(),
                coord.getX(), coord.getY());
        highlight.setStrokeWidth(3);
        highlight.setStroke(Color.RED);
        this.StreetHighlight.add(highlight);
    }

    /**
     * Returns street id (name probably)
     * @return street id
     */
    public java.lang.String getId(){ return this.id; }

    /**
     * Returns list of Coordinates which defines the street
     * @return list of defining coordinates
     */
    public java.util.List<Coordinate> getCoordinates(){ return this.coords; }

    /**
     * Tells the street begin
     * @return coordinate of the first defining point
     */
    public Coordinate begin(){ return this.coords.get(0); }

    /**
     * Tells the street end
     * @return coordinate of the last defining point
     */
    public Coordinate end(){ return this.coords.get(coords.size() - 1); }

    /**
     * Returns list of stops that belongs to the street
     * @return list of stops attached to the street
     */
    public java.util.List<Stop> getStops(){ return this.stops; }

    /**
     * Checks if two streets follow each other
     * @param s is the street to be checked
     * @return true if any defining point of the given street
     * is the same as any defining point of this street
     */
    public boolean follows(Street s){
        for (Coordinate coord : this.coords) {
            for (int j = 0; j < s.getCoordinates().size(); j++) {
                if (coord.equals(s.getCoordinates().get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Draws the street and its all stops to given Pane
     * @param mapCanvas is the Pane where the street is going to be drawn
     */
    public void draw(Pane mapCanvas){
        for(int i = 1; i < coords.size(); i ++){
            Coordinate begin = coords.get(i - 1);
            Coordinate end = coords.get(i);
            begin.draw(mapCanvas);
            end.draw(mapCanvas);
            if(!this.drawn.get(i -1)) {
                Line tmpLine =  StreetLines.get(i - 1);
                tmpLine.setOnMouseClicked(t -> this.highlightToggle(mapCanvas));
                mapCanvas.getChildren().add(tmpLine);
                this.drawn.set(i-1, true);
            }
        }
        for (Stop stop : stops) {
            stop.draw(mapCanvas);
        }
        this.wholeStreetDrawn = true;
    }

    /**
     * Erases the street with its all stops from given Pane
     * @param mapCanvas is the Pane where the street will disappear
     */
    public void erase(Pane mapCanvas){
        this.highlightOff(mapCanvas);
        for(int i = 0; i < this.StreetLines.size(); i++){
            mapCanvas.getChildren().remove(StreetLines.get(i));
            this.drawn.set(i, false);
        }
        for (Stop stop : this.stops) {
            stop.erase(mapCanvas);
        }
        this.wholeStreetDrawn = false;
    }

    /**
     * Returns true if the street is visible (probably on mapCanvas)
     * @return true if the street is visible
     */
    public boolean getDrawn(){ return this.wholeStreetDrawn; }

    /**
     * Returns true if the street highlight is visible (probably on mapCanvas)
     * @return true if the street highlight is visible
     */
    public boolean getHighlightStatus(){ return this.highlightStatus; }

    /**
     * Makes highlight visible on mapCanvas
     * @param mapCanvas is the Pane where the highlight is going to be drawn
     */
    public void highlightOn(Pane mapCanvas){
        this.erase(mapCanvas);
        if(!this.highlightStatus){
            for (Line line : this.StreetHighlight) {
                mapCanvas.getChildren().add(line);
            }
            this.highlightStatus = true;
        }
        this.end().erase(mapCanvas);
        this.highlightEnd.relocate(this.end().getX() - 5, this.end().getY() - 5);
        this.highlightEnd.setFill(Color.RED);
        mapCanvas.getChildren().add(this.highlightEnd);
        this.end().draw(mapCanvas);
        this.draw(mapCanvas);
    }

    /**
     * Makes highlight disappear on mapCanvas
     * @param mapCanvas is the Pane where the highlight is going to be erased
     */
    public void highlightOff(Pane mapCanvas){
        if(this.highlightStatus){
            for (Line line : this.StreetHighlight) {
                mapCanvas.getChildren().remove(line);
            }
            mapCanvas.getChildren().remove(this.highlightEnd);
            this.highlightStatus = false;
        }
    }

    /**
     * Toggles highlight on mapCanvas (if its on it turns it of ect.)
     * @param mapCanvas is the Pane where the highlight will be toggled
     */
    public void highlightToggle(Pane mapCanvas){
        if(this.highlightStatus){
            this.highlightOff(mapCanvas);
        } else {
            this.highlightOn(mapCanvas);
        }
    }

    /**
     * Moves whole street with specified values
     * @param mapCanvas is the Pane where the street is going to be moved
     * @param x is the movement in X axis
     * @param y is the movement in Y axis
     */
    public void moveStreet(Pane mapCanvas, int x, int y){
        if(this.wholeStreetDrawn){
            this.erase(mapCanvas);
        }
        this.begin().moveCoord(mapCanvas, x, y);
        for(int i = 0; i < this.StreetLines.size(); i++){
            Coordinate begin = this.coords.get(i);
            Coordinate end = this.coords.get(i+1);
            end.moveCoord(mapCanvas, x, y);
            Line line = new Line(begin.getX(), begin.getY(), end.getX(), end.getY());
            line.setStrokeWidth(1);
            line.setStroke(Color.BLACK.deriveColor(1, 1, 1, 0.7));
            this.StreetLines.set(i, line);
            Line highlight = new Line(begin.getX(), begin.getY(), end.getX(), end.getY());
            highlight.setStrokeWidth(3);
            highlight.setStroke(Color.RED);
            this.StreetHighlight.set(i, highlight);
        }
        for (Stop stop : this.stops) {
            stop.moveStop(mapCanvas, x, y);
        }
        if(!this.wholeStreetDrawn){
            this.draw(mapCanvas);
        }
    }

    /**
     * Tell shortest point on street to mouse cursor *-- my loved masterpiece*
     * @param mouse is the position of the mouse in coordinate structure (or any other position)
     * @return the closest coordinate to given coordinate "mouse" that is on the street
     */
    public Coordinate shortestPointToCoord(Coordinate mouse){
        Coordinate result = null;
        for(int i = 1 ; i < this.coords.size(); i++){
            Coordinate A = new Coordinate(this.coords.get(i - 1).getX(), this.coords.get(i - 1).getY());
            Coordinate B = new Coordinate(this.coords.get(i).getX(), this.coords.get(i).getY());
            int a = B.getY() - A.getY();
            int b = -(B.getX() - A.getX());
            int c = -a * A.getX() - b * A.getY();
            Coordinate C = new Coordinate(mouse.getX()-a*(a*mouse.getX()+b*mouse.getY()+c)/(a*a + b*b),
                    mouse.getY()-b*(a*mouse.getX()+b*mouse.getY()+c)/(a*a + b*b));
            int epsilon = 1;
            if(A.distance(C) + C.distance(B) - epsilon < A.distance(B)){
                if(result == null || C.distance(mouse) < result.distance(mouse)){
                    result = C;
                }
            } else if(A.distance(C) > A.distance(B)){
                if(result == null || B.distance(mouse) < result.distance(mouse)){
                    result = B;
                }
            } else {
                if(result == null || A.distance(mouse) < result.distance(mouse)){
                    result = A;
                }
            }
        }
        return result;
    }

    /**
     * Removes last coord of street (must remain at least 2 coords) with all stops on this segment
     * @param mapCanvas is the Pane where the street is drawn
     */
    public void removeLastCoord(Pane mapCanvas){
        if(this.coords.size() > 2){
            this.coords.get(this.coords.size() - 1).erase(mapCanvas);
            this.coords.remove(this.coords.size() - 1);
            this.drawn.remove(this.drawn.size() - 1);
            this.StreetLines.remove(this.StreetLines.size() - 1);
            this.StreetHighlight.remove(this.StreetHighlight.size() - 1);
            this.streetRoute.remove(this.streetRoute.size() - 1);
            this.streetRouteType.remove(this.streetRouteType.size() - 1);
            if(this.streetRouteType.get(this.streetRouteType.size() - 1).equals("stop")){
                this.streetRoute.remove(this.streetRoute.size() - 1);
                this.streetRouteType.remove(this.streetRouteType.size() - 1);
            }
        }
        for(int i = 0; i < this.stops.size(); i++){
            if(!this.isStopOnStreet(this.stops.get(i))){
                this.stops.get(i).erase(mapCanvas);
                this.stops.remove(i);
                i--;
            }
        }
    }

    /**
     * Removes stop at given coordinates from the street
     * @param c is the coordinate of stop that's going to be removed
     * @param mapCanvas is the Pane where the street is drawn
     */
    public void removeStop(Coordinate c, Pane mapCanvas){
        for(int i = 0; i < this.stops.size(); i++){
            if(this.stops.get(i).getCoord().equals(c)){
                this.stops.get(i).erase(mapCanvas);
                this.stops.get(i).removeProcedure();
                this.stops.remove(i);
                break;
            }
        }
        for(int i = 0; i < this.streetRoute.size(); i++){
            if(this.streetRoute.get(i).equals(c) && this.streetRouteType.get(i).equals("stop")){
                this.streetRoute.remove(i);
                this.streetRouteType.remove(i);
                break;
            }
        }
    }
}