package sample;


import javafx.scene.Scene;
// import javafx.scene.canvas.Canvas;
// import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.maps.Coordinate;
import map.maps.Map;
import map.maps.Stop;
import map.maps.Street;
import javafx.application.Application;
import static java.lang.StrictMath.abs;

public class Main extends Application {

    private int dragLocationX = - 1;
    private int dragLocationY = - 1;
    private int zoomValue = 50;

    public void start(Stage primaryStage) {

        // parameters
        double menuWidth = 200;
        int snapAggression = 10;

        HBox root = new HBox();
        VBox menu = new VBox();
        Scene mainScene = new Scene(root, 1000, 600);
        Map mainMap = new Map();
        Pane overlay = new Pane();
        FileChooser fileChoose = new FileChooser();
        Rectangle stop = new Rectangle(5, 5);
        Circle highlightPoint = new Circle(5);
        // map menu buttons ect
        ToggleButton coordAdd = new ToggleButton("Add coordinate");
        Button coordRemove = new Button("Remove last coord");
        ToggleButton stopRemove = new ToggleButton("Remove stop");
        Button removeStreet = new Button("Remove street");
        ToggleButton stopAdd = new ToggleButton("Add stop");
        TextField newStopName = new TextField("name of new stop");
        ToggleButton EraseStreet = new ToggleButton("Toggle street visibility");
        ToggleButton HighlightStreet = new ToggleButton("Toggle highlight");
        ComboBox streetMenu = new ComboBox();
        Button loadMap = new Button("Load map");
        Button saveMap = new Button("Save map");

        //Canvas canvas = new Canvas(300, 300);
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        menu.setBackground(new Background(new BackgroundFill(Paint.valueOf("GRAY"), null, null)));
        // overlay.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        overlay.sceneProperty().addListener((observable, oldValue, newValue) -> {
            overlay.prefWidthProperty().bind(newValue.widthProperty());
            overlay.prefHeightProperty().bind(newValue.heightProperty());
        });

        overlay.setOnMouseClicked(event -> {
            if(HighlightStreet.isSelected()){
                HighlightStreet.fire();
            }
            mainMap.highlightOffAll(overlay);
            if(coordAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null) {
                Coordinate snapCoord = Coordinate.create(0,0);
                for(int i = 0; i < mainMap.getStreets().size(); i++){
                    Street tmpStreet = mainMap.getStreets().get(i);
                    for(int j = 0; j < tmpStreet.getCoordinates().size() ; j++){
                        Coordinate tmpCoord = tmpStreet.getCoordinates().get(j);
                        if(abs(tmpCoord.getX() - event.getX()) < snapAggression && abs(tmpCoord.getY() - event.getY()) < snapAggression){
                            snapCoord = tmpCoord;
                            System.out.println("snaping");
                        }
                    }
                }
                assert snapCoord != null;
                if(mainMap.isStreet((String) streetMenu.getValue())){
                    if(snapCoord.getX() == 0 && snapCoord.getY() == 0){
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).addCoord(Coordinate.create(((int) event.getX()), ((int) event.getY())));
                    } else {
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).addCoord(snapCoord);
                    }
                } else {
                    if(snapCoord.getX() == 0 && snapCoord.getY() == 0){
                        Coordinate tmpCoord = Coordinate.create(((int) event.getX()), ((int) event.getY()));
                        assert tmpCoord != null;
                        tmpCoord.draw(overlay);
                        mainMap.addStreet(new Street((String) streetMenu.getValue(), tmpCoord));
                    } else {
                        mainMap.addStreet(new Street((String) streetMenu.getValue(), snapCoord));
                    }
                    updateStreetMenu(streetMenu, mainMap);
                    streetMenu.getSelectionModel().selectLast();
                }
                mainMap.draw(overlay);
            } else if(stopAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null &&
                    newStopName.getText() != null && !newStopName.getText().equals("")){
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                System.out.println(mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).addStop(new Stop(newStopName.getText(),
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord))));
                stopAdd.fire();
                overlay.getChildren().remove(stop);
                mainMap.draw(overlay);
            } else if(stopRemove.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null){
                Coordinate tmp;
                Coordinate result = null;
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                double distance = 999999;
                for(int i = 0; i < mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().size(); i++){
                    tmp = mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().get(i).getCoord();
                    if(tmp.distance(mouseCoord) < distance){
                        result = tmp;
                        distance = tmp.distance(mouseCoord);
                    }
                }
                for(int i = 0; i < mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().size(); i++){
                    tmp = mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().get(i).getCoord();
                    if(tmp.equals(result)){
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().get(i).erase(overlay);
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().remove(i);
                    }
                }
                mainMap.moveMap(overlay, 0, 0);
                stopRemove.fire();
                overlay.getChildren().remove(highlightPoint);
            }
        });
        overlay.setOnMousePressed(event ->{
            this.dragLocationX = (int) event.getX();
            this.dragLocationY = (int) event.getY();
        });
        overlay.setOnMouseExited(event -> overlay.getChildren().remove(highlightPoint));
        overlay.setOnMouseDragged(event ->{
           if(!coordAdd.isSelected() && !stopAdd.isSelected()){
               mainMap.moveMap(overlay, ((int) event.getX() - this.dragLocationX), ((int) event.getY() - this.dragLocationY));
               this.dragLocationX = (int) event.getX();
               this.dragLocationY = (int) event.getY();
           }
        });
        overlay.setOnMouseMoved(event ->{
            if(stopAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null){
                overlay.getChildren().removeAll(stop);
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                Coordinate result = mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord);
                stop.setStroke(Color.BLACK);
                stop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
                stop.relocate(result.getX() - 3, result.getY() - 3);
                overlay.getChildren().addAll(stop);

            } else if(stopRemove.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null &&
                    mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().size() > 0){
                Coordinate tmp;
                Coordinate result = null;
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                double distance = 999999;
                for(int i = 0; i < mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().size(); i++){
                    tmp = mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().get(i).getCoord();
                    if(tmp.distance(mouseCoord) < distance){
                        result = tmp;
                        distance = tmp.distance(mouseCoord);
                    }
                }
                overlay.getChildren().remove(highlightPoint);
                highlightPoint.relocate(result.getX() - 5, result.getY() - 5);
                highlightPoint.setFill(Color.RED);
                overlay.getChildren().add(highlightPoint);
            }
        });

        // TODO continue wit zoom function
        overlay.setOnScroll(event -> {
            if(event.getDeltaY() != 0){
                if(event.getDeltaY() > 0){
                    if(this.zoomValue <= 95){ this.zoomValue += 5; }
                } else {
                    if(this.zoomValue >= 10){ this.zoomValue -= 5; }
                }
                System.out.println("zoom detected " + this.zoomValue + " mouse location x:" + event.getX() + " y:" + event.getY());
            }
        });

        coordAdd.setPrefWidth(menuWidth);

        coordRemove.setPrefWidth(menuWidth);
        coordRemove.setOnAction(event -> {
            if(streetMenu.getValue() != "" && streetMenu.getValue() != null &&  mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getCoordinates().size() > 2){
                overlay.getChildren().clear();
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).removeLastCoord(overlay);
                mainMap.moveMap(overlay, 0, 0);
            }
        });

        stopAdd.setPrefWidth(menuWidth);
        stopAdd.setOnAction(event ->{
            if(!stopAdd.isSelected()){
                overlay.getChildren().remove(stop);
            }
        });

        stopRemove.setPrefWidth(menuWidth);

        removeStreet.setPrefWidth(menuWidth);
        removeStreet.setOnAction(event ->{
            if(mainMap.isStreet((String) streetMenu.getValue())){
                overlay.getChildren().clear();
                mainMap.getStreets().remove(mainMap.getMapPointerById((String) streetMenu.getValue()));
                mainMap.moveMap(overlay, 0, 0);
                updateStreetMenu(streetMenu, mainMap);
            }
        });

        EraseStreet.setPrefWidth(menuWidth);
        EraseStreet.setOnAction(event -> {
            if(HighlightStreet.isSelected()){
                HighlightStreet.fire();
            }
            if(!mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getDrawn()){
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).draw(overlay);
            } else {
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).erase(overlay);
            }
        });

        HighlightStreet.setPrefWidth(menuWidth);
        HighlightStreet.setOnAction(event -> {
            if(EraseStreet.isSelected()){
                EraseStreet.fire();
            }
            mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).highlightToggle(overlay);
        });

        loadMap.setPrefWidth(menuWidth);
        loadMap.setOnAction(event -> {
            if(coordAdd.isSelected()){
                coordAdd.fire();
                coordAdd.setSelected(false);
            }
            if(stopAdd.isSelected()){
                stopAdd.fire();
                stopAdd.setSelected(false);
            }
            if(EraseStreet.isSelected()){
                EraseStreet.fire();
                EraseStreet.setSelected(false);
                mainMap.draw(overlay);
            }
            if(HighlightStreet.isSelected()){
                HighlightStreet.fire();
                HighlightStreet.setSelected(false);
                mainMap.highlightOffAll(overlay);
            }
            if(stopRemove.isSelected()){
                stopRemove.fire();
                stopRemove.setSelected(false);
            }
            try {
                mainMap.loadMapFromFile(fileChoose.showOpenDialog(primaryStage).getPath(), overlay);
                updateStreetMenu(streetMenu, mainMap);
                mainMap.draw(overlay);
            }
            catch (NullPointerException e){
                System.out.println("no file choosed");
            }
        });

        saveMap.setPrefWidth(menuWidth);
        saveMap.setOnAction(event -> {
            if(coordAdd.isSelected()){
                coordAdd.fire();
                coordAdd.setSelected(false);
            }
            if(stopAdd.isSelected()){
                stopAdd.fire();
                stopAdd.setSelected(false);
            }
            if(EraseStreet.isSelected()){
                EraseStreet.fire();
                EraseStreet.setSelected(false);
                mainMap.draw(overlay);
            }
            if(HighlightStreet.isSelected()){
                HighlightStreet.fire();
                HighlightStreet.setSelected(false);
                mainMap.highlightOffAll(overlay);
            }
            if(stopRemove.isSelected()){
                stopRemove.fire();
                stopRemove.setSelected(false);
            }
            try{
                mainMap.saveMapToFile(fileChoose.showSaveDialog(primaryStage).getPath());
            }
            catch (NullPointerException e){
                System.out.println("no file choosed");
            }
        });

        streetMenu.setPrefWidth(menuWidth);
        newStopName.setText("name of new stop");

        menu.getChildren().addAll(coordAdd, coordRemove, stopAdd, newStopName, stopRemove, removeStreet, EraseStreet, HighlightStreet, streetMenu, loadMap, saveMap); // map menu
        menu.setMinWidth(menuWidth);
        root.getChildren().addAll(overlay, menu);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("IJA project");
        primaryStage.show();
        // overlay.setMaxWidth((primaryStage.getWidth()/100) * 80);
        // overlay.setMinWidth((primaryStage.getWidth()/100) * 80);

        mainMap.loadMapFromFile("sample.txt", overlay);
        updateStreetMenu(streetMenu, mainMap);
        mainMap.draw(overlay);
    }

    private void updateStreetMenu(ComboBox streetMenu, Map map){
        streetMenu.getItems().clear();
        for(int i = 0; i < map.getStreets().size(); i++){
            streetMenu.getItems().add(i, map.getStreets().get(i).getId());
        }
        streetMenu.getSelectionModel().selectFirst();
        streetMenu.setEditable(true);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
