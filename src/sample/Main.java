package sample;



import javafx.scene.Scene;
// import javafx.scene.canvas.Canvas;
// import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

        AtomicBoolean addCoord = new AtomicBoolean(false);
        HBox root = new HBox();
        VBox menu = new VBox();
        Scene mainScene = new Scene(root, 1000, 600);
        Map m1 = new Map();
        Pane overlay = new Pane();
        ComboBox streetMenu = new ComboBox();

        //Canvas canvas = new Canvas(300, 300);
        //GraphicsContext gc = canvas.getGraphicsContext2D();

        overlay.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        overlay.setMaxWidth(700);
        overlay.setMinWidth(700);
        overlay.setOnMouseClicked(event -> {
            m1.highlightOffAll(overlay);
            if(addCoord.get()) {
                Coordinate snapCoord = Coordinate.create(0,0);
                for(int i = 0; i < m1.getStreets().size(); i++){
                    Street tmpStreet = m1.getStreets().get(i);
                    for(int j = 0; j < tmpStreet.getCoordinates().size() ; j++){
                        Coordinate tmpCoord = tmpStreet.getCoordinates().get(j);
                        if(abs(tmpCoord.getX() - event.getX()) < 10 && abs(tmpCoord.getY() - event.getY()) < 10){
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

        Button coordAdd = new Button("Add coordinate");
        coordAdd.setOnAction(event -> {
            if(addCoord.get()){
                addCoord.set(false);
                coordAdd.setText("Add coordinates");
            } else {
                addCoord.set(true);
                coordAdd.setText("Stop adding coordinates");
            }
        });

        // test of erasing coordinate
        Button EraseStreet = new Button("toggle street visibility");
        EraseStreet.setOnAction(event -> {
            if(!m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).getDrawn()){
                m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).draw(overlay);
            } else {
                m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).erase(overlay);
            }
        });

        Button HighlightStreet = new Button("toggle highlight");
        HighlightStreet.setOnAction(event -> m1.getStreets().get(m1.getMapPointerById((String) streetMenu.getValue())).highlightToggle(overlay));

        // end test

        menu.getChildren().addAll(coordAdd, EraseStreet, HighlightStreet, streetMenu);

        root.getChildren().addAll(overlay, menu);

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("IJA project");
        primaryStage.show();

        /* unimportant test field */
        Coordinate c1 = Coordinate.create(20, 20);
        Coordinate c2 = Coordinate.create(20, 200);
        Coordinate c3 = Coordinate.create(320, 200);
        Coordinate c4 = Coordinate.create(320, 100);
        Coordinate c5 = Coordinate.create(100, 100);
        Coordinate c6 = Coordinate.create(20, 100);
        Coordinate c7 = Coordinate.create(520, 400);
        Coordinate c8 = Coordinate.create(120, 400);
        Coordinate c9 = Coordinate.create(120, 350);
        Coordinate c10 = Coordinate.create(20, 300);
        Coordinate c11 = Coordinate.create(200, 500);
        Coordinate c12 = Coordinate.create(200, 250);
        Coordinate c13 = Coordinate.create(210, 270);
        Coordinate c14 = Coordinate.create(210, 150);
        Coordinate c15 = Coordinate.create(400, 150);

        Street s1 = new Street("ulice1", c1);
        Street s2 = new Street("ulice2", c7);
        Street s3 = new Street("ulice3", c11);
        assert c6 != null;
        Stop st1 = new Stop("Hviezdoslavova", c6);
        s1.addCoord(c2);
        s1.addCoord(c3);
        s1.addCoord(c4);
        s1.addCoord(c5);
        s1.addStop(st1);
        s2.addCoord(c8);
        s2.addCoord(c9);
        s2.addCoord(c10);
        s3.addCoord(c12);
        s3.addCoord(c13);
        s3.addCoord(c14);
        s3.addCoord(c15);

        m1.addStreet(s1);
        m1.addStreet(s2);
        m1.addStreet(s3);

        m1.draw(overlay);
        m1.loadMapFromFile("testmap1.txt");
        updateStreetMenu(streetMenu, m1);

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
