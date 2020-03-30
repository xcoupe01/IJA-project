package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.concurrent.atomic.AtomicBoolean;
import static java.lang.StrictMath.abs;

// TODO make street stop creatable from window

public class Main extends Application {

    private int dragLocationX = - 1;
    private int dragLocationY = - 1;
    private int zoomValue = 50;

    public void start(Stage primaryStage) {

        // parameters
        double menuWidth = 200;
        int snapAggression = 10;

        AtomicBoolean addCoord = new AtomicBoolean(false);
        AtomicBoolean addStop = new AtomicBoolean(false);
        HBox root = new HBox();
        VBox menu = new VBox();
        Scene mainScene = new Scene(root, 1000, 600);
        Map m1 = new Map();
        Pane overlay = new Pane();
        ComboBox streetMenu = new ComboBox();
        FileChooser fileChoose = new FileChooser();
        Rectangle stop = new Rectangle(5, 5);
        // menu buttons ect
        ToggleButton coordAdd = new ToggleButton("Add coordinate");
        ToggleButton stopAdd = new ToggleButton("Add stop");
        TextField newStopName = new TextField("name of new stop");
        Button EraseStreet = new Button("Toggle street visibility");
        Button HighlightStreet = new Button("Toggle highlight");
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
            m1.highlightOffAll(overlay);
            if(addCoord.get() && streetMenu.getValue() != "" && streetMenu.getValue() != null) {
                Coordinate snapCoord = Coordinate.create(0,0);
                for(int i = 0; i < m1.getStreets().size(); i++){
                    Street tmpStreet = m1.getStreets().get(i);
                    for(int j = 0; j < tmpStreet.getCoordinates().size() ; j++){
                        Coordinate tmpCoord = tmpStreet.getCoordinates().get(j);
                        if(abs(tmpCoord.getX() - event.getX()) < snapAggression && abs(tmpCoord.getY() - event.getY()) < snapAggression){
                            snapCoord = tmpCoord;
                            System.out.println("snaping");
                        }
                    }
                }
                assert snapCoord != null;
                if(m1.isStreet((String) streetMenu.getValue())){
                    if(snapCoord.getX() == 0 && snapCoord.getY() == 0){
                        m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).addCoord(Coordinate.create(((int) event.getX()), ((int) event.getY())));
                    } else {
                        m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).addCoord(snapCoord);
                    }
                } else {
                    if(snapCoord.getX() == 0 && snapCoord.getY() == 0){
                        Coordinate tmpCoord = Coordinate.create(((int) event.getX()), ((int) event.getY()));
                        assert tmpCoord != null;
                        tmpCoord.draw(overlay);
                        m1.addStreet(new Street((String) streetMenu.getValue(), tmpCoord));
                    } else {
                        m1.addStreet(new Street((String) streetMenu.getValue(), snapCoord));
                    }
                    updateStreetMenu(streetMenu, m1);
                    streetMenu.getSelectionModel().selectLast();
                }
                m1.draw(overlay);
            } else if(addStop.get() && streetMenu.getValue() != "" && streetMenu.getValue() != null &&
                    newStopName.getText() != null && newStopName.getText() != ""){
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).addStop(new Stop(newStopName.getText(),
                        m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord)));
                stopAdd.fire();
                overlay.getChildren().remove(stop);
                m1.draw(overlay);
            }
        });
        overlay.setOnMousePressed(event ->{
            this.dragLocationX = (int) event.getX();
            this.dragLocationY = (int) event.getY();
        });
        overlay.setOnMouseDragged(event ->{
           if(!addCoord.get()){
               m1.moveMap(overlay, ((int) event.getX() - this.dragLocationX), ((int) event.getY() - this.dragLocationY));
               this.dragLocationX = (int) event.getX();
               this.dragLocationY = (int) event.getY();
           }
        });
        overlay.setOnMouseMoved(event ->{
            if(addStop.get() && streetMenu.getValue() != "" && streetMenu.getValue() != null){
                overlay.getChildren().removeAll(stop);
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                Coordinate result = m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord);
                stop.setStroke(Color.BLACK);
                stop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
                stop.relocate(result.getX() - 3, result.getY() - 3);
                overlay.getChildren().addAll(stop);

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
        coordAdd.setOnAction(event -> {
            if(addCoord.get()){
                addCoord.set(false);
            } else {
                addCoord.set(true);
            }
        });


        stopAdd.setPrefWidth(menuWidth);
        stopAdd.setOnAction(event -> {
            if(addStop.get()){
                addStop.set(false);
                menu.getChildren().clear();
                menu.getChildren().addAll(coordAdd, stopAdd, EraseStreet, HighlightStreet, streetMenu, loadMap, saveMap);
                overlay.getChildren().remove(stop);
            } else {
                addStop.set(true);
                newStopName.setText("name of new stop");
                menu.getChildren().clear();
                menu.getChildren().addAll(coordAdd, stopAdd, newStopName, EraseStreet, HighlightStreet, streetMenu, loadMap, saveMap);
            }
        });


        EraseStreet.setPrefWidth(menuWidth);
        EraseStreet.setOnAction(event -> {
            if(!m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).getDrawn()){
                m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).draw(overlay);
            } else {
                m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).erase(overlay);
            }
        });


        HighlightStreet.setPrefWidth(menuWidth);
        HighlightStreet.setOnAction(event -> m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).highlightToggle(overlay));


        loadMap.setPrefWidth(menuWidth);
        loadMap.setOnAction(event -> {
            try {
                m1.loadMapFromFile(fileChoose.showOpenDialog(primaryStage).getPath(), overlay);
                updateStreetMenu(streetMenu, m1);
                m1.draw(overlay);
            }
            catch (NullPointerException e){
                System.out.println("no file choosed");
            }
        });


        saveMap.setPrefWidth(menuWidth);
        saveMap.setOnAction(event -> {
            try{
                m1.saveMapToFile(fileChoose.showSaveDialog(primaryStage).getPath());
            }
            catch (NullPointerException e){
                System.out.println("no file choosed");
            }
        });

        streetMenu.setPrefWidth(menuWidth);

        menu.getChildren().addAll(coordAdd, stopAdd, EraseStreet, HighlightStreet, streetMenu, loadMap, saveMap);
        menu.setMinWidth(menuWidth);
        root.getChildren().addAll(overlay, menu);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("IJA project");
        primaryStage.show();
        // overlay.setMaxWidth((primaryStage.getWidth()/100) * 80);
        // overlay.setMinWidth((primaryStage.getWidth()/100) * 80);

        m1.loadMapFromFile("sample.txt", overlay);
        updateStreetMenu(streetMenu, m1);
        m1.draw(overlay);

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
