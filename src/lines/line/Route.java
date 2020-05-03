package lines.line;

import javafx.scene.layout.Pane;
import lines.Iline.iRoute;
import map.maps.Coordinate;
import javafx.scene.shape.Line;
import map.maps.Map;


public class Route implements iRoute {
    private java.util.List<Coordinate> coords = new java.util.ArrayList<>();
    private java.util.List<java.lang.String> type = new java.util.ArrayList<>();
    private java.util.List<Line> lineHighlight = new java.util.ArrayList<>();
    private PTLine line;
    private boolean drawn = false;
    private Map map;

    Route(Map map, PTLine line){
        this.line = line;
        this.map = map;
        line.setRoute(this);
    }

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

    public boolean completeRoute(){ return this.type.get(0).equals("stop") && this.type.get(this.type.size() - 1).equals("stop"); }

    public java.util.List<Coordinate> getRoute(){ return this.coords; }

    public java.util.List<java.lang.String> getRouteType(){ return this.type; }

    public void draw(Pane mapCanvas){
        if(!this.drawn){
            this.drawn = true;
            for (Line line : this.lineHighlight) {
                mapCanvas.getChildren().add(line);
            }
        }
    }

    public void erase(Pane mapCanvas){
        if(this.drawn){
            this.drawn = false;
            for (Line line : this.lineHighlight) {
                mapCanvas.getChildren().remove(line);
            }
        }
    }

    public void removeLast(Pane mapCanvas){
        if(this.coords.size() > 1){
            this.coords.remove(this.coords.size() - 1);
            this.type.remove(this.type.size() - 1);
            mapCanvas.getChildren().remove(this.lineHighlight.get(this.lineHighlight.size() - 1));
            this.lineHighlight.remove(this.lineHighlight.size() - 1);
        }
    }
}