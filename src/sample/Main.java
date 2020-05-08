package sample;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lines.line.PTLine;
import lines.line.PublicTransport;
import lines.line.Vehicle;
import map.maps.Coordinate;
import map.maps.Map;
import map.maps.Stop;
import map.maps.Street;
import javafx.application.Application;
import static java.lang.StrictMath.abs;

/**
 * IJA VUT FIT Project for year 2020
 *
 * This program simulates public transport in a city. Its developed in Java 11 but Its meant to run
 * in Java SE 8 with corresponding JavaFX framework. Its fully self sufficient and does not need any other
 * files to run, but it can load specialized files ".map" and ".lines".
 *
 * This is the main class of the whole project which generates window and set up all UI elements.
 *
 * @author Vojtěch Čoupek (xcoupe01)
 * @author Tadeáš Jůza (xjuzat00)
 */
public class Main extends Application {

    /** Current drag position X axis*/
    private int dragLocationX = - 1;
    /** Current drag position Y axis*/
    private int dragLocationY = - 1;
    /** Current zoom value*/
    private int zoomValue = 50;
    /** Selected time travel minutes value*/
    private int timeTravelMinutesValue = 0;
    /** Selected time travel hours value*/
    private int timeTravelHoursValue = 0;

    //TODO zoom mapCanvas with ScrollPane
    //TODO add button collision control

    /**
     * Start of the whole application, sets all UI elements and sets the main objects
     * @param primaryStage is the window to be used
     */
    public void start(Stage primaryStage) {

        // parameters
        double menuWidth = 200;
        int snapAggression = 10;

        HBox root = new HBox();
        VBox menu = new VBox();
        Scene mainScene = new Scene(root, 1000, 600);
        Map mainMap = new Map();
        PublicTransport mainPubTrans = new PublicTransport(mainMap);
        mainMap.attachPubTrans(mainPubTrans);
        Pane overlay = new Pane();
        FileChooser fileChoose = new FileChooser();
        Rectangle stop = new Rectangle(5, 5);
        Circle highlightPoint = new Circle(5);
        //main menu
        HBox menuListChooser = new HBox();
        ToggleButton mapMenuButton = new ToggleButton("Map");
        ToggleButton lineMenuButton = new ToggleButton("Lines");
        ToggleButton overviewButton = new ToggleButton("Main");
        VBox mapMenu = new VBox();
        VBox lineMenu = new VBox();
        VBox overview = new VBox();
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
        Label mapMenuLabel = new Label("\n Every time when map is \n edited, all lines data \n are discarded and reset");
        // line menu buttons ect
        ToggleButton highlightAllLineRoutes = new ToggleButton("Highlight Public Transport");
        Button loadPublicTransp = new Button("Load Public Transport");
        Button savePublicTransp = new Button("Save Public Transport");
        ComboBox linesMenu = new ComboBox();
        ToggleButton addLinePoint = new ToggleButton("Add line route point");
        Button removeLastLinePoint = new Button("Remove last route point");
        ToggleButton toggleLineHighlight = new ToggleButton("Highlight Line Route");
        Button deleteLine = new Button("Delete line");
        Button addLine = new Button("Add line");
        ColorPicker newLineColor = new ColorPicker();
        ToggleButton addVehicle = new ToggleButton("Add vehicle");
        ToggleButton removeVehicle = new ToggleButton("Remove vehicle");
        // overview list
        Label timeDisplay = new Label();
        Button pauseButton = new Button("Pause");
        Button runButton = new Button("Run");
        Slider animationSpeed = new Slider(1, 150, 100);
        Pane lineInformationPane = new Pane();
        ScrollPane lineInformationPaneWrap = new ScrollPane();
        Button realTimeAnimationSpeed = new Button("Real time speed");
        HBox timeTravelMenuButtons = new HBox();
        HBox timeTravelButtons = new HBox();
        MenuButton timeTravelHours = new MenuButton();
        MenuButton timeTravelMinutes = new MenuButton();
        Button timeTravelPast = new Button("To past");
        Button timeTravelFuture = new Button("To future");

        // kills animator thread when application is closed
        primaryStage.setOnCloseRequest(event -> mainPubTrans.setStopAnimator(true));

        // information Pane settings
        lineInformationPaneWrap.setContent(lineInformationPane);
        //lineInformationPane.setStyle("-fx-background-color: WHITE");
        Text lineInformationPaneStartText = new Text("This window will show\ndetail information if you\nclick on vehicle or stop");
        lineInformationPaneStartText.setY(50);
        lineInformationPaneStartText.setX(20);
        lineInformationPane.getChildren().add(lineInformationPaneStartText);
        lineInformationPaneWrap.fitToHeightProperty().set(true);
        lineInformationPaneWrap.setPrefWidth(menuWidth);
        mainMap.attachInformationPane(lineInformationPane);
        lineInformationPane.setPrefHeight(menuWidth);
        lineInformationPane.setPrefWidth(10);

        // timer settings
        mainPubTrans.setTimeDisplay(timeDisplay);
        mainPubTrans.updateTimeDisplay();
        timeDisplay.setPrefHeight(runButton.getPrefHeight());

        // main menu builder
        mapMenuButton.setPrefWidth(menuWidth/3);
        lineMenuButton.setPrefWidth(menuWidth/3);
        overviewButton.setPrefWidth(menuWidth/3);

        mapMenu.getChildren().addAll(menuListChooser, streetMenu, coordAdd, coordRemove,
                stopAdd, newStopName, stopRemove, removeStreet, EraseStreet, HighlightStreet,
                loadMap, saveMap, mapMenuLabel);

        lineMenu.getChildren().addAll(highlightAllLineRoutes, linesMenu, addLinePoint,
                removeLastLinePoint, addVehicle, removeVehicle, toggleLineHighlight, deleteLine,
                addLine, newLineColor, loadPublicTransp, savePublicTransp);

        overview.getChildren().addAll(timeDisplay, runButton, pauseButton, animationSpeed,
                realTimeAnimationSpeed, timeTravelMenuButtons, timeTravelButtons, lineInformationPaneWrap);

        // real time animation button settings
        realTimeAnimationSpeed.setPrefWidth(menuWidth);
        realTimeAnimationSpeed.setOnAction(event -> mainPubTrans.setAnimationStepDelay(mainPubTrans.getTickMeansSec() * 1000));

        // animation speed slider settings
        animationSpeed.setPrefHeight(runButton.getPrefHeight());

        // menu bar button settings
        mapMenuButton.setOnAction(event -> {
            if(mapMenuButton.isSelected()){
                if(lineMenuButton.isSelected()){
                    lineMenuButton.fire();
                }
                if(overviewButton.isSelected()){
                    overviewButton.fire();
                }
                menu.getChildren().clear();
                menu.getChildren().addAll(menuListChooser, mapMenu);
            } else {
                menu.getChildren().clear();
                menu.getChildren().add(menuListChooser);
            }
        });

        lineMenuButton.setOnAction(event -> {
            if(lineMenuButton.isSelected()){
                if(mapMenuButton.isSelected()){
                    mapMenuButton.fire();
                }
                if(overviewButton.isSelected()){
                    overviewButton.fire();
                }
                menu.getChildren().clear();
                menu.getChildren().addAll(menuListChooser, lineMenu);
            } else {
                menu.getChildren().clear();
                menu.getChildren().add(menuListChooser);
            }
        });

        overviewButton.setOnAction(event -> {
            if(overviewButton.isSelected()){
                if(mapMenuButton.isSelected()){
                    mapMenuButton.fire();
                }
                if(lineMenuButton.isSelected()){
                    lineMenuButton.fire();
                }
                menu.getChildren().clear();
                menu.getChildren().addAll(menuListChooser, overview);
            } else {
                menu.getChildren().clear();
                menu.getChildren().add(menuListChooser);
            }
        });

        menuListChooser.getChildren().addAll(overviewButton, mapMenuButton, lineMenuButton);
        menu.setBackground(new Background(new BackgroundFill(Paint.valueOf("GRAY"), null, null)));

        // time travel settings
        for(int i = 0; i < 24; i++){
            int finalI = i;
            MenuItem tmpMenuItem = new MenuItem(String.valueOf(i));
            tmpMenuItem.setOnAction(event -> {
                timeTravelHours.setText(String.valueOf(finalI));
                this.timeTravelHoursValue = finalI;
            });
            timeTravelHours.getItems().add(i, tmpMenuItem);
        }
        for(int i = 0; i < 60; i++){
            int finalI = i;
            MenuItem tmpMenuItem = new MenuItem();
            if(i < 10){
                tmpMenuItem.setText("0".concat(String.valueOf(i)));
                tmpMenuItem.setOnAction(event -> {
                    timeTravelMinutes.setText("0".concat(String.valueOf(finalI)));
                    this.timeTravelMinutesValue = finalI;
                });
            } else {
                tmpMenuItem.setText(String.valueOf(i));
                tmpMenuItem.setOnAction(event -> {
                    timeTravelMinutes.setText(String.valueOf(finalI));
                    this.timeTravelMinutesValue = finalI;
                });
            }
            timeTravelMinutes.getItems().add(i, tmpMenuItem);
        }
        timeTravelHours.setPrefWidth((menuWidth/2) - 5);
        timeTravelMinutes.setPrefWidth((menuWidth/2) - 5);
        timeTravelHours.getItems().get(0).fire();
        timeTravelMinutes.getItems().get(0).fire();
        Label timeTravelDots = new Label(" : ");
        timeTravelDots.setPrefHeight(timeTravelHours.getPrefHeight());
        timeTravelMenuButtons.getChildren().addAll(timeTravelHours, timeTravelDots, timeTravelMinutes);
        timeTravelButtons.getChildren().addAll(timeTravelPast, timeTravelFuture);
        timeTravelFuture.setOnAction(event -> {
            mainPubTrans.setStopAnimator(true);
            while(!(mainPubTrans.getTimer().getHours() == this.timeTravelHoursValue &&
                    mainPubTrans.getTimer().getMinutes() == this.timeTravelMinutesValue)){
                mainPubTrans.animationStep();
            }
        });
        timeTravelPast.setOnAction(event -> {
            mainPubTrans.setStopAnimator(true);
            mainPubTrans.switchDirectionAllVehicles();
            while(!(mainPubTrans.getTimer().getHours() == this.timeTravelHoursValue &&
                    mainPubTrans.getTimer().getMinutes() == this.timeTravelMinutesValue)){
                mainPubTrans.oppositeAnimationStep();
            }
            mainPubTrans.switchDirectionAllVehicles();
        });
        timeTravelPast.setPrefWidth(menuWidth/2);
        timeTravelFuture.setPrefWidth(menuWidth/2);


        overlay.sceneProperty().addListener((observable, oldValue, newValue) -> {
            overlay.prefWidthProperty().bind(newValue.widthProperty());
            overlay.prefHeightProperty().bind(newValue.heightProperty());
        });

        overlay.setOnMouseClicked(event -> {
            if(HighlightStreet.isSelected()){
                HighlightStreet.fire();
            }
            mainMap.highlightOffAll(overlay);
            if(mapMenuButton.isSelected() && coordAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null) {
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
            } else if(mapMenuButton.isSelected()&& stopAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null &&
                    newStopName.getText() != null && !newStopName.getText().equals("")){
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).addStop(new Stop(newStopName.getText(),
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord),
                        mainPubTrans, lineInformationPane, mainMap));
                stopAdd.fire();
                overlay.getChildren().remove(stop);
                mainMap.draw(overlay);
            } else if(mapMenuButton.isSelected() && stopRemove.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null){
                Coordinate tmp;
                Coordinate result = null;
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                double distance = Double.POSITIVE_INFINITY;
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
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).removeStop(result, overlay);
                    }
                }
                mainMap.moveMap(overlay, 0, 0);
                stopRemove.fire();
                overlay.getChildren().remove(highlightPoint);
            } else if(addLinePoint.isSelected() && lineMenuButton.isSelected()){
                double distance = Double.POSITIVE_INFINITY;
                Coordinate mouseCoord = new Coordinate((int) event.getX(), (int) event.getY());
                Coordinate tmpCoord = new Coordinate(0, 0);
                boolean isItPoint = false;
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        for(int j = 0; j < mainMap.getStreets().size(); j++){
                            for(int k = 0; k < mainMap.getStreets().get(j).getCoordinates().size(); k++){
                                if(mainPubTrans.getLines().get(i).getRoute().canAdd(mainMap.getStreets().get(j).getCoordinates().get(k), "point")){
                                    if(mouseCoord.distance(mainMap.getStreets().get(j).getCoordinates().get(k)) < distance){
                                        distance = mouseCoord.distance(mainMap.getStreets().get(j).getCoordinates().get(k));
                                        tmpCoord = mainMap.getStreets().get(j).getCoordinates().get(k);
                                        isItPoint = true;
                                    }
                                }
                            }
                            for(int k = 0; k < mainMap.getStreets().get(j).getStops().size(); k++){
                                if(mainPubTrans.getLines().get(i).getRoute().canAdd(mainMap.getStreets().get(j).getStops().get(k).getCoord(), "stop")){
                                    if(mouseCoord.distance(mainMap.getStreets().get(j).getStops().get(k).getCoord()) < distance){
                                        distance = mouseCoord.distance(mainMap.getStreets().get(j).getStops().get(k).getCoord());
                                        tmpCoord = mainMap.getStreets().get(j).getStops().get(k).getCoord();
                                        isItPoint = false;
                                    }
                                }
                            }
                        }
                        if(isItPoint){
                            mainPubTrans.getLines().get(i).getRoute().addPoint(tmpCoord);
                        } else {
                            mainPubTrans.getLines().get(i).getRoute().addStop(tmpCoord);
                        }
                        mainPubTrans.updatePTPos(overlay, 0, 0);
                    }
                }
            } else if(addVehicle.isSelected() && lineMenuButton.isSelected()){
                double distance = Double.POSITIVE_INFINITY;
                Coordinate mouseCoord = new Coordinate((int) event.getX(), (int) event.getY());
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        int pos = 0;
                        for(int j = 0; j < mainPubTrans.getLines().get(i).getRoute().getRoute().size(); j++){
                            if(mainPubTrans.getLines().get(i).getRoute().getRoute().get(j).distance(mouseCoord) < distance){
                                pos = j;
                                distance = mainPubTrans.getLines().get(i).getRoute().getRoute().get(j).distance(mouseCoord);
                            }
                        }
                        mainPubTrans.getLines().get(i).addVehicle(pos, mainPubTrans);
                        mainPubTrans.getLines().get(i).getVehicles().get(mainPubTrans.getLines().get(i).getVehicles().size() - 1).draw(overlay);
                        break;
                    }
                }

            } else if(removeVehicle.isSelected() && lineMenuButton.isSelected()){
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        mainPubTrans.getLines().get(i).removeNearestVehicle(new Coordinate((int) event.getX(), (int) event.getY()), overlay);
                        highlightPoint.relocate(-20, -20);
                        break;
                    }
                }
            }
        });
        overlay.setOnMousePressed(event ->{
            this.dragLocationX = (int) event.getX();
            this.dragLocationY = (int) event.getY();
        });
        overlay.setOnMouseExited(event -> overlay.getChildren().remove(highlightPoint));
        overlay.setOnMouseDragged(event ->{
           if(!coordAdd.isSelected() && !stopAdd.isSelected() && !addLinePoint.isSelected()){
               mainMap.moveMap(overlay, ((int) event.getX() - this.dragLocationX), ((int) event.getY() - this.dragLocationY));
               mainPubTrans.updatePTPos(overlay, ((int) event.getX() - this.dragLocationX), ((int) event.getY() - this.dragLocationY));
               this.dragLocationX = (int) event.getX();
               this.dragLocationY = (int) event.getY();
           }
        });
        overlay.setOnMouseMoved(event ->{
            if(mapMenuButton.isSelected() && stopAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null){
                overlay.getChildren().removeAll(stop);
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                Coordinate result = mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord);
                stop.setStroke(Color.BLACK);
                stop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
                stop.relocate(result.getX() - 3, result.getY() - 3);
                overlay.getChildren().addAll(stop);

            } else if(mapMenuButton.isSelected() && stopRemove.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null &&
                    mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().size() > 0){
                Coordinate tmp;
                Coordinate result = null;
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                double distance = Double.POSITIVE_INFINITY;
                for(int i = 0; i < mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().size(); i++){
                    tmp = mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getStops().get(i).getCoord();
                    if(tmp.distance(mouseCoord) < distance){
                        result = tmp;
                        distance = tmp.distance(mouseCoord);
                    }
                }
                overlay.getChildren().remove(highlightPoint);
                assert result != null;
                highlightPoint.relocate(result.getX() - 5, result.getY() - 5);
                highlightPoint.setFill(Color.RED);
                overlay.getChildren().add(highlightPoint);
            } else if(addLinePoint.isSelected() && lineMenuButton.isSelected()){
                double distance = Double.POSITIVE_INFINITY;
                Coordinate mouseCoord = new Coordinate((int) event.getX(), (int) event.getY());
                Coordinate tmpCoord = new Coordinate(0, 0);
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        for(int j = 0; j < mainMap.getStreets().size(); j++){
                            for(int k = 0; k < mainMap.getStreets().get(j).getCoordinates().size(); k++){
                                if(mainPubTrans.getLines().get(i).getRoute().canAdd(mainMap.getStreets().get(j).getCoordinates().get(k), "point")){
                                    if(mouseCoord.distance(mainMap.getStreets().get(j).getCoordinates().get(k)) < distance){
                                        distance = mouseCoord.distance(mainMap.getStreets().get(j).getCoordinates().get(k));
                                        tmpCoord = mainMap.getStreets().get(j).getCoordinates().get(k);
                                    }
                                }
                            }
                            for(int k = 0; k < mainMap.getStreets().get(j).getStops().size(); k++){
                                if(mainPubTrans.getLines().get(i).getRoute().canAdd(mainMap.getStreets().get(j).getStops().get(k).getCoord(), "stop")){
                                    if(mouseCoord.distance(mainMap.getStreets().get(j).getStops().get(k).getCoord()) < distance){
                                        distance = mouseCoord.distance(mainMap.getStreets().get(j).getStops().get(k).getCoord());
                                        tmpCoord = mainMap.getStreets().get(j).getStops().get(k).getCoord();
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                highlightPoint.setFill(Paint.valueOf("RED"));
                highlightPoint.relocate(tmpCoord.getX() - 5, tmpCoord.getY() - 5);
                overlay.getChildren().remove(highlightPoint);
                overlay.getChildren().add(highlightPoint);
            } else if(addVehicle.isSelected() && lineMenuButton.isSelected()){
                double distance = Double.POSITIVE_INFINITY;
                Coordinate mouseCoord = new Coordinate((int) event.getX(), (int) event.getY());
                Coordinate tmpCoord = new Coordinate(0, 0);
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        for(int j = 0; j < mainPubTrans.getLines().get(i).getRoute().getRoute().size(); j++){
                            if(mainPubTrans.getLines().get(i).getRoute().getRoute().get(j).distance(mouseCoord) < distance){
                                tmpCoord = mainPubTrans.getLines().get(i).getRoute().getRoute().get(j);
                                distance = mainPubTrans.getLines().get(i).getRoute().getRoute().get(j).distance(mouseCoord);
                            }
                        }
                        break;
                    }
                }
                highlightPoint.setFill(Paint.valueOf("RED"));
                highlightPoint.relocate(tmpCoord.getX() - 5, tmpCoord.getY() - 5);
                overlay.getChildren().remove(highlightPoint);
                overlay.getChildren().add(highlightPoint);
            } else if(removeVehicle.isSelected() && lineMenuButton.isSelected()){
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        Vehicle tmpVehicle = mainPubTrans.getLines().get(i).getNearestVehicle(new Coordinate((int) event.getX(), (int) event.getY()));
                        if(tmpVehicle != null){
                            highlightPoint.relocate(tmpVehicle.getPosition().getX() - 5, tmpVehicle.getPosition().getY() - 5);
                            highlightPoint.setFill(Paint.valueOf("RED"));
                            overlay.getChildren().remove(highlightPoint);
                            overlay.getChildren().add(highlightPoint);
                        }
                        break;
                    }
                }
            }
        });

        // TODO continue with zoom function
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

        highlightAllLineRoutes.setPrefWidth(menuWidth);
        highlightAllLineRoutes.setOnAction(event -> mainPubTrans.toggleHighlight(overlay));

        loadPublicTransp.setPrefWidth(menuWidth);
        loadPublicTransp.setOnAction(event -> {
            try {
                mainPubTrans.loadPTFromFile(overlay, fileChoose.showOpenDialog(primaryStage).getPath(), mainMap, lineInformationPane);
                updateLinesMenu(linesMenu, mainPubTrans, addLine);
            } catch (NullPointerException e){
                System.out.println("no file choosed");
            }
        });

        savePublicTransp.setPrefWidth(menuWidth);
        savePublicTransp.setOnAction(event -> {
            try {
                mainPubTrans.savePTToFile(fileChoose.showSaveDialog(primaryStage).getPath(), mainMap);
            } catch (NullPointerException e){
                System.out.println("no file choosed");
            }
        });

        addLinePoint.setPrefWidth(menuWidth);

        removeLastLinePoint.setPrefWidth(menuWidth);
        removeLastLinePoint.setOnAction(event -> {
            for(int i = 0; i < linesMenu.getItems().size(); i++ ){
                if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                    mainPubTrans.getLines().get(i).getRoute().removeLast(overlay);
                    mainPubTrans.updatePTPos(overlay, 0, 0);
                    break;
                }
            }
        });

        toggleLineHighlight.setPrefWidth(menuWidth);
        toggleLineHighlight.setOnAction(event -> {
            if(highlightAllLineRoutes.isSelected()){
                highlightAllLineRoutes.fire();
                highlightAllLineRoutes.setSelected(false);
            }
            for(int i = 0; i < linesMenu.getItems().size(); i++ ){
                if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                    mainPubTrans.getLines().get(i).toggleLineHighlight(overlay);
                    break;
                }
            }
        });

        deleteLine.setPrefWidth(menuWidth);
        deleteLine.setOnAction(event -> {
            for(int i = 0; i < linesMenu.getItems().size(); i++ ){
                if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                    if(toggleLineHighlight.isSelected()){
                        toggleLineHighlight.fire();
                        toggleLineHighlight.setSelected(false);
                    }
                    mainPubTrans.getLines().remove(i);
                    updateLinesMenu(linesMenu, mainPubTrans, addLine);
                    break;
                }
            }
        });

        newLineColor.setPrefWidth(menuWidth);

        addLine.setPrefWidth(menuWidth);
        addLine.setOnAction(event -> {
            for(int i = 1; true; i++){
                boolean found = false;
                for(int j = 0; j < mainPubTrans.getLines().size(); j++){
                    if(mainPubTrans.getLines().get(j).getLineNumber() == i){
                        found = true;
                    }
                }
                if(!found){
                    mainPubTrans.addLine(new PTLine(i, newLineColor.getValue(), mainMap));
                    updateLinesMenu(linesMenu, mainPubTrans, addLine);
                    break;
                }
            }
        });

        removeVehicle.setPrefWidth(menuWidth);
        addVehicle.setPrefWidth(menuWidth);

        pauseButton.setPrefWidth(menuWidth);
        pauseButton.setOnAction(event -> mainPubTrans.setStopAnimator(true));
        runButton.setPrefWidth(menuWidth);
        runButton.setOnAction(event -> mainPubTrans.playAnimator());
        //animationSpeed.setShowTickMarks(true);
        //animationSpeed.setShowTickLabels(true);
        animationSpeed.setOnMouseDragged(event -> mainPubTrans.setAnimationStepDelay((int) animationSpeed.getValue()));

        linesMenu.setPrefWidth(menuWidth);
        streetMenu.setPrefWidth(menuWidth);
        newStopName.setText("name of new stop");
        mapMenuLabel.setPrefWidth(menuWidth);

        menu.getChildren().add(menuListChooser);
        menu.setSpacing(5);
        overviewButton.fire();
        menu.setMinWidth(menuWidth);
        root.getChildren().addAll(overlay, menu);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("IJA project");
        primaryStage.show();
        // overlay.setMaxWidth((primaryStage.getWidth()/100) * 80);
        // overlay.setMinWidth((primaryStage.getWidth()/100) * 80);

        mainMap.loadMapFromFile("sample2.map", overlay);
        mainPubTrans.loadPTFromFile(overlay, "sample3.line", mainMap, lineInformationPane);
        updateStreetMenu(streetMenu, mainMap);
        updateLinesMenu(linesMenu, mainPubTrans, addLine);
        mainMap.draw(overlay);

    }

    /**
     * Update street combo box menu to current map situation
     * @param streetMenu is the combo box to be set
     * @param map is the map to to be set from
     */
    private void updateStreetMenu(ComboBox streetMenu, Map map){
        streetMenu.getItems().clear();
        for(int i = 0; i < map.getStreets().size(); i++){
            streetMenu.getItems().add(i, map.getStreets().get(i).getId());
        }
        streetMenu.getSelectionModel().selectFirst();
        streetMenu.setEditable(true);
    }

    /**
     * Update lines combo box menu to current lines situation and sets new line button to new value
     * @param linesMenu is the combo box to be set
     * @param PT is the public transport
     * @param newLineNum is the new line button
     */
    private void updateLinesMenu(ComboBox linesMenu, PublicTransport PT, Button newLineNum){
        linesMenu.getItems().clear();
        for(int i = 0; i < PT.getLines().size(); i++){
            linesMenu.getItems().add(i, "line number ".concat(String.valueOf(PT.getLines().get(i).getLineNumber())));
        }
        linesMenu.getSelectionModel().selectFirst();
        for(int i = 1; true; i++){
            boolean found = false;
            for(int j = 0; j < PT.getLines().size(); j++){
                if(PT.getLines().get(j).getLineNumber() == i){
                    found = true;
                }
            }
            if(!found){
                newLineNum.setText("Add new line number ".concat(String.valueOf(i)));
                break;
            }
        }
    }

    /**
     * Main function
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
