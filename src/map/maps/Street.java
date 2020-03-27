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
    private java.util.List<Line> StreetHighlight = new java.util.ArrayList<>();
    private java.lang.String id;                                                //< Street id (probably name)
    private boolean wholeStreetDrawn;
    private boolean highlightStatus;
    private Circle highlightEnd = new Circle(5);

    public Street(java.lang.String id, Coordinate begin){
        this.id = id;
        this.coords.add(begin);
        this.wholeStreetDrawn = false;
        this.highlightStatus = false;
        this.highlightEnd.setFill(Color.RED);
    }

    // add stop to the street
    public boolean addStop(Stop stop){
        for(int i = 1; i < this.coords.size(); i++){
            if(this.coords.get(i - 1).distance(this.coords.get(i)) == (this.coords.get(i-1).distance(stop.getCoord()) + this.coords.get(i).distance(stop.getCoord()))){
                this.stops.add(stop);
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
        Line line = new Line(this.coords.get(this.coords.size() - 2).getX(), this.coords.get(this.coords.size() - 2).getY(),
                coord.getX(), coord.getY());
        line.setStrokeWidth(1);
        line.setStroke(Color.BLACK.deriveColor(1, 1, 1, 0.7));
        StreetLines.add(line);
        Line highlight = new Line(this.coords.get(this.coords.size() - 2).getX(), this.coords.get(this.coords.size() - 2).getY(),
                coord.getX(), coord.getY());
        highlight.setStrokeWidth(3);
        highlight.setStroke(Color.RED);
        StreetHighlight.add(highlight);
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
    // TODO rework so it chcecks all coordinates, not just end and begin of the street
    public boolean follows(Street s){
        return (this.begin().equals(s.begin()) || this.begin().equals(s.end()) || this.end().equals(s.begin()) || this.end().equals(s.end()));
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
    public boolean getDrawn(){ return this.wholeStreetDrawn; }

    public boolean getHighlightStatus(){ return this.highlightStatus; }

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

    public void highlightOff(Pane mapCanvas){
        if(this.highlightStatus){
            for (Line line : this.StreetHighlight) {
                mapCanvas.getChildren().remove(line);
            }
            mapCanvas.getChildren().remove(this.highlightEnd);
            this.highlightStatus = false;
        }
    }

    public void highlightToggle(Pane mapCanvas){
        if(this.highlightStatus){
            this.highlightOff(mapCanvas);
        } else {
            this.highlightOn(mapCanvas);
        }
    }

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

    // TODO complete zoom function
    public void zoomStreet(Pane mapCanvas, int mouseX, int mouseY, int zoom){
        if(this.wholeStreetDrawn){
            this.erase(mapCanvas);
        }

    }
}