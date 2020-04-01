package map.maps;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.Imaps.iStreet;


public class Street implements iStreet{

    private java.util.List<Coordinate> coords = new java.util.ArrayList<>();    //< List of Coordinates
    private java.util.List<Stop> stops = new java.util.ArrayList<>();           //< List of Stops
    private java.util.List<Boolean> drawn = new java.util.ArrayList<>();        //< List that tells which parts of Street are visible
    private java.util.List<Line> StreetLines = new java.util.ArrayList<>();     //< List of graphical lines
    private java.util.List<Line> StreetHighlight = new java.util.ArrayList<>(); //< List of highlighted lines
    private java.util.List<Coordinate> streetRoute = new java.util.ArrayList<>();
    private java.util.List<java.lang.String> streetRouteType = new java.util.ArrayList<>();
    private java.lang.String id;                                                //< Street id (probably name)
    private boolean wholeStreetDrawn;                                           //< tells if the street is visible
    private boolean highlightStatus;                                            //< tells if the highlight is visible
    private Circle highlightEnd = new Circle(5);                             //< highlight end point of street

    public Street(java.lang.String id, Coordinate begin){
        this.id = id;
        this.coords.add(begin);
        this.wholeStreetDrawn = false;
        this.highlightStatus = false;
        this.highlightEnd.setFill(Color.RED);
        this.streetRoute.add(begin);
        this.streetRouteType.add("point");
    }

    // add stop to the street
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

    public java.util.List<Coordinate> getStreetRoute(){ return this.streetRoute;}

    public java.util.List<java.lang.String> getStreetRouteType(){ return this.streetRouteType;}

    // tells if coordinate is on the street
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

    // adds new coordination for the street, also creates line object to be drawn
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

    // returns street id (name probably)
    public java.lang.String getId(){ return this.id; }

    // returns list of Coordinates of the Street
    public java.util.List<Coordinate> getCoordinates(){ return this.coords; }

    // tells the street begin
    public Coordinate begin(){ return this.coords.get(0); }

    // tells the street end
    public Coordinate end(){ return this.coords.get(coords.size() - 1); }

    // returns list of Stops that belongs to the street
    public java.util.List<Stop> getStops(){ return this.stops; }

    // checks if two streets follow each other
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

    // draws the Street to given Pane
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

    // erases the Street from given Pane
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

    // returns boolean value if the street is visible on mapCanvas
    public boolean getDrawn(){ return this.wholeStreetDrawn; }

    // returns boolean value if the street highlight is visible on mapCanvas
    public boolean getHighlightStatus(){ return this.highlightStatus; }

    // makes highlight visible on mapCanvas
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

    // makes highlight disappear on mapCanvas
    public void highlightOff(Pane mapCanvas){
        if(this.highlightStatus){
            for (Line line : this.StreetHighlight) {
                mapCanvas.getChildren().remove(line);
            }
            mapCanvas.getChildren().remove(this.highlightEnd);
            this.highlightStatus = false;
        }
    }

    // toggles highlight on mapCanvas
    public void highlightToggle(Pane mapCanvas){
        if(this.highlightStatus){
            this.highlightOff(mapCanvas);
        } else {
            this.highlightOn(mapCanvas);
        }
    }

    // moves whole street with specified values
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

    // future zoom function
    // TODO complete zoom function
    public void zoomStreet(Pane mapCanvas, Coordinate mouse, int zoom){
        if(this.wholeStreetDrawn){
            this.erase(mapCanvas);
        }

    }

    // tell shortest point on street to mouse cursor -- my loved masterpiece
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

    // removes last coord of street (must remain at least 2 coords) with all stops on this segment
    public void removeLastCoord(Pane mapCanvas){
        if(this.coords.size() > 2){
            this.coords.get(this.coords.size() - 1).erase(mapCanvas);
            this.coords.remove(this.coords.size() - 1);
            this.drawn.remove(this.drawn.size() - 1);
            this.StreetLines.remove(this.StreetLines.size() - 1);
            this.StreetHighlight.remove(this.StreetHighlight.size() - 1);
        }
        for(int i = 0; i < this.stops.size(); i++){
            if(!this.isStopOnStreet(this.stops.get(i))){
                this.stops.get(i).erase(mapCanvas);
                this.stops.remove(i);
                i--;
            }
        }
    }

    public void removeStop(Coordinate c, Pane mapCanvas){
        for(int i = 0; i < this.stops.size(); i++){
            if(this.stops.get(i).getCoord().equals(c)){
                this.stops.get(i).erase(mapCanvas);
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