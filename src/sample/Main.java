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
import lines.line.*;
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
    /** Selected time travel minutes value*/
    private int timeTravelMinutesValue = 0;
    /** Selected time travel hours value*/
    private int timeTravelHoursValue = 0;
    /** Selected street segment value*/
    private int selectedSegment = 0;
    /** Selected connection value*/
    private int selectedConnection = -1;
    /** Selected connection hour value*/
    private int connectionHours = 0;
    /** Selected connection minute value*/
    private int connectionMinutes = 0;

    /**
     * Start of the whole application, sets all UI elements and sets the main objects
     * @param primaryStage is the window to be used
     */
    public void start(Stage primaryStage) {

        // parameters
        double menuWidth = 200;
        int snapAggression = 10;
        int maxTrafficVal = 5;

        // main objects of the app
        HBox root = new HBox();
        VBox menu = new VBox();
        Scene mainScene = new Scene(root, 1000, 650);
        Map mainMap = new Map();
        PublicTransport mainPubTrans = new PublicTransport(mainMap);
        mainMap.attachPubTrans(mainPubTrans);
        Pane mapCanvas = new Pane();
        ZoomableScrollPane mapCanvasWrapZoom = new ZoomableScrollPane(mapCanvas);
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
        ToggleButton eraseStreet = new ToggleButton("Toggle street visibility");
        ToggleButton highlightStreet = new ToggleButton("Toggle highlight");
        ComboBox streetMenu = new ComboBox();
        MenuButton streetSegmentChooser = new MenuButton();
        Slider trafficLevelChooser = new Slider(1, maxTrafficVal, 1);
        Button highlightSegment = new Button("Show segment");
        Button loadMap = new Button("Load map");
        Button saveMap = new Button("Save map");
        HBox segmentHBox = new HBox();
        // line menu buttons ect
        ToggleButton highlightAllLineRoutes = new ToggleButton("Highlight public transport");
        Button loadPublicTransp = new Button("Load public transport");
        Button savePublicTransp = new Button("Save public transport");
        ComboBox linesMenu = new ComboBox();
        ToggleButton addLinePoint = new ToggleButton("Add line route point");
        Button removeLastLinePoint = new Button("Remove last route point");
        ToggleButton toggleLineHighlight = new ToggleButton("Highlight line route");
        Button deleteLine = new Button("Delete line");
        Button addLine = new Button("Add line");
        ColorPicker newLineColor = new ColorPicker();
        ToggleButton addVehicle = new ToggleButton("Add vehicle");
        Button addVehicleForward = new Button("Forward");
        HBox addVehicleBox = new HBox();
        ToggleButton removeVehicle = new ToggleButton("Remove vehicle");
        Accordion scheduledConnectionList = new Accordion();
        ScrollPane scheduledConnectionListWrap = new ScrollPane(scheduledConnectionList);
        Button removeConnection = new Button("Remove connection");
        Button addConnection = new Button("Add connection");
        MenuButton connectionTimeHours = new MenuButton();
        MenuButton connectionTimeMinutes = new MenuButton();
        Button connectionDirectionButton = new Button("Forward");
        HBox connectionButtons = new HBox();
        Button generateTimeTable = new Button("Generate time table");
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

        // pack mapCanvas (mapCanvas) to overlayWrap a set the overlayWrap
        mapCanvasWrapZoom.setMinWidth(primaryStage.getWidth() - menuWidth);

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
                stopAdd, newStopName, stopRemove, removeStreet, eraseStreet, highlightStreet,
                segmentHBox, trafficLevelChooser, loadMap, saveMap);

        lineMenu.getChildren().addAll(highlightAllLineRoutes, linesMenu, addLinePoint,
                removeLastLinePoint, addVehicleBox, removeVehicle, toggleLineHighlight, deleteLine,
                addLine, newLineColor, scheduledConnectionListWrap, connectionButtons, addConnection,
                removeConnection, generateTimeTable, loadPublicTransp, savePublicTransp);

        overview.getChildren().addAll(timeDisplay, runButton, pauseButton, animationSpeed,
                realTimeAnimationSpeed, timeTravelMenuButtons, timeTravelButtons, lineInformationPaneWrap);

        // line add vehicle setting
        addVehicleBox.getChildren().addAll(addVehicle, addVehicleForward);
        addVehicle.setPrefWidth(menuWidth / 2);
        addVehicleForward.setPrefWidth(menuWidth / 2);
        addVehicleForward.setOnAction(event -> {
            if(addVehicleForward.getText().equals("Forward")){
                addVehicleForward.setText("Backward");
            } else {
                addVehicleForward.setText("Forward");
            }
        });

        // real time animation button settings
        realTimeAnimationSpeed.setPrefWidth(menuWidth);
        realTimeAnimationSpeed.setOnAction(event -> mainPubTrans.setAnimationStepDelay(mainPubTrans.getTickMeansSec() * 1000));

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

        // UI traffic selection - setup
        trafficLevelChooser.setMajorTickUnit(1);
        trafficLevelChooser.setMinorTickCount(0);
        trafficLevelChooser.setSnapToTicks(true);
        trafficLevelChooser.setShowTickMarks(true);
        trafficLevelChooser.setShowTickLabels(true);
        trafficLevelChooser.valueProperty().addListener((obs, oldval, newVal) ->
                trafficLevelChooser.setValue(newVal.intValue()));
        streetMenu.setOnAction(event -> {
            segmentChooserSetter(mainMap, streetMenu, streetSegmentChooser, trafficLevelChooser);
            eraseStreet.setSelected(!mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getDrawn());
            highlightStreet.setSelected(mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getHighlightStatus());
        });
        streetSegmentChooser.setPrefWidth(menuWidth/2);
        highlightSegment.setOnAction(event -> {
            if(this.selectedSegment == -1){
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).highlightOn(mapCanvas);
            } else {
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue()))
                        .toggleSegmentHighlight(this.selectedSegment, mapCanvas);
            }
            highlightStreet.setSelected(true);
        });
        highlightSegment.setPrefWidth(menuWidth/2);
        segmentHBox.getChildren().addAll(streetSegmentChooser, highlightSegment);
        trafficLevelChooser.setOnMouseReleased(event -> {
            if(this.selectedSegment == -1){
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).setTrafficAllToVal((int) trafficLevelChooser.getValue());
            } else {
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).setTraffic(this.selectedSegment, (int) trafficLevelChooser.getValue());
            }
        });

        // mapCanvas - listener for size setting
        mapCanvas.sceneProperty().addListener((observable, oldValue, newValue) -> {
            mapCanvas.minWidthProperty().bind(newValue.widthProperty().multiply(2));
            mapCanvas.minHeightProperty().bind(newValue.heightProperty().multiply(2));
        });

        // mapCanvasWrapZoom - listener for size setting
        mapCanvasWrapZoom.sceneProperty().addListener((observable, oldValue, newValue) -> {
            mapCanvas.prefWidthProperty().bind(newValue.widthProperty());
            mapCanvas.prefHeightProperty().bind(newValue.heightProperty());
        });

        // mapCanvas listener - mouse click functions
        mapCanvas.setOnMouseClicked(event -> {
            // when clicked on mapCanvas street highlight is erased
            if(highlightStreet.isSelected()){
                highlightStreet.fire();
            }
            mainMap.highlightOffAll(mapCanvas);
            // function - adding points to street
            if(mapMenuButton.isSelected() && coordAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null) {
                Coordinate snapCoord = Coordinate.create(0,0);
                for(int i = 0; i < mainMap.getStreets().size(); i++){
                    Street tmpStreet = mainMap.getStreets().get(i);
                    for(int j = 0; j < tmpStreet.getCoordinates().size() ; j++){
                        Coordinate tmpCoord = tmpStreet.getCoordinates().get(j);
                        if(abs(tmpCoord.getX() - event.getX()) < snapAggression && abs(tmpCoord.getY() - event.getY()) < snapAggression){
                            snapCoord = tmpCoord;
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
                        tmpCoord.draw(mapCanvas);
                        mainMap.addStreet(new Street((String) streetMenu.getValue(), tmpCoord));
                    } else {
                        mainMap.addStreet(new Street((String) streetMenu.getValue(), snapCoord));
                    }
                    updateStreetMenu(streetMenu, mainMap);
                    streetMenu.getSelectionModel().selectLast();
                }
                mainMap.draw(mapCanvas);
            // function - adding stop to street
            } else if(mapMenuButton.isSelected()&& stopAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null &&
                    newStopName.getText() != null && !newStopName.getText().equals("")){
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).addStop(new Stop(newStopName.getText(),
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord),
                        mainPubTrans, lineInformationPane, mainMap));
                stopAdd.fire();
                mapCanvas.getChildren().remove(stop);
                mainMap.draw(mapCanvas);
            // function - removing stop from street
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
                        mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).removeStop(result, mapCanvas);
                    }
                }
                mainMap.moveMap(mapCanvas, 0, 0);
                mainPubTrans.correctLineRoutes(mapCanvas);
                stopRemove.fire();
                mapCanvas.getChildren().remove(highlightPoint);
            // function - adding points to line
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
                        mainPubTrans.updatePTPos(mapCanvas, 0, 0);
                    }
                }
            // function - adding vehicles to line
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
                        mainPubTrans.getLines().get(i).addVehicle(pos, mainPubTrans, mapCanvas);
                        mainPubTrans.getLines().get(i).getVehicles().get(mainPubTrans.getLines().get(i).getVehicles().size() - 1).draw();
                        mainPubTrans.getLines().get(i).getVehicles().get(mainPubTrans.getLines().get(i).getVehicles().size() - 1).setInformationPane(lineInformationPane);
                        if(addVehicleForward.getText().equals("Backward")){
                            mainPubTrans.getLines().get(i).getVehicles().get(mainPubTrans.getLines().get(i).getVehicles().size() - 1).setForward(false);
                        }
                        break;
                    }
                }
            // function - removing vehicles from line
            } else if(removeVehicle.isSelected() && lineMenuButton.isSelected()){
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        mainPubTrans.getLines().get(i).removeNearestVehicle(new Coordinate((int) event.getX(), (int) event.getY()), mapCanvas);
                        mapCanvas.getChildren().remove(highlightPoint);
                        break;
                    }
                }
            }
        });

        // removing highlight point from mapCanvas when mouse exit the pane
        mapCanvas.setOnMouseExited(event -> mapCanvas.getChildren().remove(highlightPoint));

        // mapCanvas drag setting
        mapCanvas.setOnMousePressed(event ->{
            this.dragLocationX = (int) event.getX();
            this.dragLocationY = (int) event.getY();
        });

        // mapCanvas drag settings
        mapCanvas.setOnMouseDragged(event ->{
           if(!coordAdd.isSelected() && !stopAdd.isSelected() && !addLinePoint.isSelected()){
               mainMap.moveMap(mapCanvas, ((int) event.getX() - this.dragLocationX), ((int) event.getY() - this.dragLocationY));
               mainPubTrans.updatePTPos(mapCanvas, ((int) event.getX() - this.dragLocationX), ((int) event.getY() - this.dragLocationY));
               this.dragLocationX = (int) event.getX();
               this.dragLocationY = (int) event.getY();
           }
        });

        // mapCanvas listener - mouse move functions
        mapCanvas.setOnMouseMoved(event ->{
            // function - showing preview of add stop function
            if(mapMenuButton.isSelected() && stopAdd.isSelected() && streetMenu.getValue() != "" && streetMenu.getValue() != null){
                mapCanvas.getChildren().removeAll(stop);
                Coordinate mouseCoord = Coordinate.create((int) event.getX(), (int) event.getY());
                Coordinate result = mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).shortestPointToCoord(mouseCoord);
                stop.setStroke(Color.BLACK);
                stop.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.7));
                stop.relocate(result.getX() - 3, result.getY() - 3);
                mapCanvas.getChildren().addAll(stop);
            // function - showing stop to be remove for remove stop function
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
                mapCanvas.getChildren().remove(highlightPoint);
                assert result != null;
                highlightPoint.relocate(result.getX() - 5, result.getY() - 5);
                highlightPoint.setFill(Color.RED);
                mapCanvas.getChildren().add(highlightPoint);
            // function - showing point to be added for line add point function
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
                mapCanvas.getChildren().remove(highlightPoint);
                mapCanvas.getChildren().add(highlightPoint);
            // function - showing preview for add vehicle to line function
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
                mapCanvas.getChildren().remove(highlightPoint);
                mapCanvas.getChildren().add(highlightPoint);
            // function - showing preview for remove vehicle from line function
            } else if(removeVehicle.isSelected() && lineMenuButton.isSelected()){
                for (int i = 0; i < linesMenu.getItems().size(); i++){
                    if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                        Vehicle tmpVehicle = mainPubTrans.getLines().get(i).getNearestVehicle(new Coordinate((int) event.getX(), (int) event.getY()));
                        if(tmpVehicle != null){
                            highlightPoint.relocate(tmpVehicle.getPosition().getX() - 5, tmpVehicle.getPosition().getY() - 5);
                            highlightPoint.setFill(Paint.valueOf("RED"));
                            mapCanvas.getChildren().remove(highlightPoint);
                            mapCanvas.getChildren().add(highlightPoint);
                        }
                        break;
                    }
                }
            }
        });

        // menu width setting for all buttons and others
        coordAdd.setPrefWidth(menuWidth);
        coordRemove.setPrefWidth(menuWidth);
        stopAdd.setPrefWidth(menuWidth);
        stopRemove.setPrefWidth(menuWidth);
        removeStreet.setPrefWidth(menuWidth);
        eraseStreet.setPrefWidth(menuWidth);
        highlightStreet.setPrefWidth(menuWidth);
        loadMap.setPrefWidth(menuWidth);
        saveMap.setPrefWidth(menuWidth);
        highlightAllLineRoutes.setPrefWidth(menuWidth);
        loadPublicTransp.setPrefWidth(menuWidth);
        savePublicTransp.setPrefWidth(menuWidth);
        addLinePoint.setPrefWidth(menuWidth);
        removeLastLinePoint.setPrefWidth(menuWidth);
        toggleLineHighlight.setPrefWidth(menuWidth);
        deleteLine.setPrefWidth(menuWidth);
        newLineColor.setPrefWidth(menuWidth);
        addLine.setPrefWidth(menuWidth);
        removeVehicle.setPrefWidth(menuWidth);
        pauseButton.setPrefWidth(menuWidth);
        runButton.setPrefWidth(menuWidth);
        menu.setMinWidth(menuWidth);
        linesMenu.setPrefWidth(menuWidth);
        streetMenu.setPrefWidth(menuWidth);
        addConnection.setPrefWidth(menuWidth);
        removeConnection.setPrefWidth(menuWidth);
        generateTimeTable.setPrefWidth(menuWidth);

        // other UI buttons settings - pretty self explanatory
        coordRemove.setOnAction(event -> {
            if(streetMenu.getValue() != "" && streetMenu.getValue() != null &&  mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getCoordinates().size() > 2){
                mapCanvas.getChildren().clear();
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).removeLastCoord(mapCanvas);
                mainMap.moveMap(mapCanvas, 0, 0);
                mainPubTrans.correctLineRoutes(mapCanvas);
                mainPubTrans.setAllVehiclesDrawn(false);
                mainPubTrans.drawAllVehicles();
            }
        });

        stopAdd.setOnAction(event ->{
            if(!stopAdd.isSelected()){
                mapCanvas.getChildren().remove(stop);
            }
        });

        coordAdd.setOnAction(event -> {
            stopAdd.setSelected(false);
            stopRemove.setSelected(false);
        });

        stopAdd.setOnAction(event -> {
            coordAdd.setSelected(false);
            stopRemove.setSelected(false);
        });

        stopRemove.setOnAction(event -> {
            coordAdd.setSelected(false);
            stopAdd.setSelected(false);
        });

        addLinePoint.setOnAction(event -> {
            addVehicle.setSelected(false);
            removeVehicle.setSelected(false);
        });
        addVehicle.setOnAction(event -> {
            addLinePoint.setSelected(false);
            removeVehicle.setSelected(false);
        });
        removeVehicle.setOnAction(event -> {
            addVehicle.setSelected(false);
            addLinePoint.setSelected(false);
        });

        removeStreet.setOnAction(event ->{
            if(mainMap.isStreet((String) streetMenu.getValue())){
                for(int i = 0; i < mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getCoordinates().size(); i++){
                    mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getCoordinates().get(i).erase(mapCanvas);
                }
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).erase(mapCanvas);
                mainMap.getStreets().remove(mainMap.getMapPointerById((String) streetMenu.getValue()));
                mainMap.moveMap(mapCanvas, 0, 0);
                updateStreetMenu(streetMenu, mainMap);
            }
            mainPubTrans.correctLineRoutes(mapCanvas);
        });

        eraseStreet.setOnAction(event -> {
            if(highlightStreet.isSelected()){
                highlightStreet.fire();
            }
            if(!mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getDrawn()){
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).draw(mapCanvas);
            } else {
                mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).erase(mapCanvas);
            }
        });

        highlightStreet.setOnAction(event -> {
            if(eraseStreet.isSelected()){
                eraseStreet.fire();
            }
            mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).highlightToggle(mapCanvas);
        });

        loadMap.setOnAction(event -> {
            if(coordAdd.isSelected()){
                coordAdd.fire();
                coordAdd.setSelected(false);
            }
            if(stopAdd.isSelected()){
                stopAdd.fire();
                stopAdd.setSelected(false);
            }
            if(eraseStreet.isSelected()){
                eraseStreet.fire();
                eraseStreet.setSelected(false);
                mainMap.draw(mapCanvas);
            }
            if(highlightStreet.isSelected()){
                highlightStreet.fire();
                highlightStreet.setSelected(false);
                mainMap.highlightOffAll(mapCanvas);
            }
            if(stopRemove.isSelected()){
                stopRemove.fire();
                stopRemove.setSelected(false);
            }
            try {
                if(!mainMap.loadMapFromFile(fileChoose.showOpenDialog(primaryStage).getPath(), mapCanvas)){
                    System.out.println("Warning - map file cannot be loaded properly, check the file!");
                }
                updateStreetMenu(streetMenu, mainMap);
                mainMap.draw(mapCanvas);
            }
            catch (NullPointerException e){
                System.out.println("Warning - no file choosed");
            }
        });

        saveMap.setOnAction(event -> {
            if(coordAdd.isSelected()){
                coordAdd.fire();
                coordAdd.setSelected(false);
            }
            if(stopAdd.isSelected()){
                stopAdd.fire();
                stopAdd.setSelected(false);
            }
            if(eraseStreet.isSelected()){
                eraseStreet.fire();
                eraseStreet.setSelected(false);
                mainMap.draw(mapCanvas);
            }
            if(highlightStreet.isSelected()){
                highlightStreet.fire();
                highlightStreet.setSelected(false);
                mainMap.highlightOffAll(mapCanvas);
            }
            if(stopRemove.isSelected()){
                stopRemove.fire();
                stopRemove.setSelected(false);
            }
            try{
                mainMap.saveMapToFile(fileChoose.showSaveDialog(primaryStage).getPath());
            }
            catch (NullPointerException e){
                System.out.println("Warning - no file choosed");
            }
        });

        highlightAllLineRoutes.setOnAction(event -> {
            toggleLineHighlight.setSelected(false);
            mainPubTrans.toggleHighlight(mapCanvas);
        });

        loadPublicTransp.setOnAction(event -> {
            try {
                if(!mainPubTrans.loadPTFromFile(mapCanvas, fileChoose.showOpenDialog(primaryStage).getPath(), mainMap, lineInformationPane)){
                    System.out.println("Warning - line file cannot be loaded properly, check the file!");
                }
                updateLinesMenu(linesMenu, mainPubTrans, addLine);
            } catch (NullPointerException e){
                System.out.println("Warning - no file choosed");
            }
        });

        savePublicTransp.setOnAction(event -> {
            try {
                mainPubTrans.savePTToFile(fileChoose.showSaveDialog(primaryStage).getPath(), mainMap);
            } catch (NullPointerException e){
                System.out.println("Warning - no file choosed");
            }
        });

        removeLastLinePoint.setOnAction(event -> {
            for(int i = 0; i < linesMenu.getItems().size(); i++ ){
                if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                    mainPubTrans.getLines().get(i).getRoute().removeLast(mapCanvas);
                    mainPubTrans.updatePTPos(mapCanvas, 0, 0);
                    break;
                }
            }
        });

        toggleLineHighlight.setOnAction(event -> {
            if(highlightAllLineRoutes.isSelected()){
                highlightAllLineRoutes.fire();
                highlightAllLineRoutes.setSelected(false);
            }
            for(int i = 0; i < linesMenu.getItems().size(); i++ ){
                if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                    mainPubTrans.getLines().get(i).toggleLineHighlight(mapCanvas);
                    break;
                }
            }
        });

        deleteLine.setOnAction(event -> {
            for(int i = 0; i < linesMenu.getItems().size(); i++ ){
                if(linesMenu.getItems().get(i).equals(linesMenu.getValue())){
                    if(toggleLineHighlight.isSelected()){
                        toggleLineHighlight.fire();
                        toggleLineHighlight.setSelected(false);
                    }
                    mainPubTrans.getLines().get(i).eraseVehicles();
                    mainPubTrans.getLines().remove(i);
                    updateLinesMenu(linesMenu, mainPubTrans, addLine);
                    break;
                }
            }
        });

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

        // animation speed slider settings
        animationSpeed.setPrefHeight(runButton.getPrefHeight());
        pauseButton.setOnAction(event -> mainPubTrans.setStopAnimator(true));
        runButton.setOnAction(event -> mainPubTrans.playAnimator());
        //animationSpeed.setShowTickMarks(true);
        //animationSpeed.setShowTickLabels(true);
        animationSpeed.setOnMouseDragged(event -> mainPubTrans.setAnimationStepDelay((int) animationSpeed.getValue()));
        newStopName.setText("name of new stop");

        // initialization of main menu
        menu.getChildren().add(menuListChooser);
        menu.setSpacing(5);
        overviewButton.fire();

        // initialization of widow nodes
        root.getChildren().addAll(mapCanvasWrapZoom, menu);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("IJA project");
        primaryStage.show();

        // initial map load
        if(!mainMap.loadMapFromFile("data/maps/sample3.map", mapCanvas)){
            System.out.println("Warning - map file cannot be loaded properly, check the file!");
        }

        // initial line load
        if(!mainPubTrans.loadPTFromFile(mapCanvas, "data/lines/sample3.line", mainMap, lineInformationPane)){
            System.out.println("Warning - line file cannot be loaded properly, check the file!");
        }


        // initial updates of choosers
        updateStreetMenu(streetMenu, mainMap);
        updateLinesMenu(linesMenu, mainPubTrans, addLine);
        segmentChooserSetter(mainMap, streetMenu, streetSegmentChooser, trafficLevelChooser);

        // initial map draw
        mainMap.draw(mapCanvas);

        // initial values settings
        mainPubTrans.setTickMeansSec(10);
        mainPubTrans.setTicksAtStop(20);

        // scheduled connections Accordion setup and buttons setup
        scheduledConnectionList.setPrefWidth(menuWidth);
        scheduledConnectionList.setPrefHeight(menuWidth/2);
        scheduledConnectionListWrap.setPrefHeight(menuWidth);
        scheduledConnectionListWrap.setFitToWidth(true);
        for (int i = 0; i < linesMenu.getItems().size(); i++) {
            if (linesMenu.getItems().get(i).equals(linesMenu.getValue())) {
                updateScheduledConnectionList(scheduledConnectionList, mainPubTrans.getLines().get(i));
                break;
            }
        }
        linesMenu.setOnAction(event -> {
            for (int i = 0; i < linesMenu.getItems().size(); i++) {
                if (linesMenu.getItems().get(i).equals(linesMenu.getValue())) {
                    updateScheduledConnectionList(scheduledConnectionList, mainPubTrans.getLines().get(i));
                    if(mainPubTrans.allLineHighlightsDrawn()){
                        highlightAllLineRoutes.setSelected(true);
                        toggleLineHighlight.setSelected(false);
                    } else if(mainPubTrans.getLines().get(i).getDrawn()){
                        highlightAllLineRoutes.setSelected(false);
                        toggleLineHighlight.setSelected(true);
                    } else {
                        highlightAllLineRoutes.setSelected(false);
                        toggleLineHighlight.setSelected(false);
                    }
                    break;
                }
            }
        });
        connectionTimeHours.getItems().clear();
        connectionTimeMinutes.getItems().clear();
        for(int i = 0; i < 24; i++){
            int finalI = i;
            MenuItem tmpMenuItem = new MenuItem(String.valueOf(i));
            tmpMenuItem.setOnAction(event -> {
                this.connectionHours = finalI;
                connectionTimeHours.setText(String.valueOf(finalI));
            });
            connectionTimeHours.getItems().add(tmpMenuItem);
        }
        for(int i = 0; i < 60; i++){
            int finalI = i;
            MenuItem tmpMenuItem = new MenuItem();
            if(i < 10){
                tmpMenuItem.setText("0".concat(String.valueOf(i)));
                tmpMenuItem.setOnAction(event ->{
                    this.connectionMinutes = finalI;
                    connectionTimeMinutes.setText("0".concat(String.valueOf(finalI)));
                });
            } else {
                tmpMenuItem.setText(String.valueOf(i));
                tmpMenuItem.setOnAction(event ->{
                    this.connectionMinutes = finalI;
                    connectionTimeMinutes.setText(String.valueOf(finalI));
                });
            }
            connectionTimeMinutes.getItems().addAll(tmpMenuItem);
        }
        connectionDirectionButton.setPrefWidth(menuWidth/3);
        connectionTimeHours.setPrefWidth(menuWidth/3);
        connectionTimeMinutes.setPrefWidth(menuWidth/3);
        connectionDirectionButton.setOnAction(event -> {
            if(connectionDirectionButton.getText().equals("Forward")){
                connectionDirectionButton.setText("Backward");
            } else {
                connectionDirectionButton.setText("Forward");
            }
        });
        connectionButtons.getChildren().addAll(connectionTimeHours, connectionTimeMinutes, connectionDirectionButton);
        connectionTimeHours.getItems().get(0).fire();
        connectionTimeMinutes.getItems().get(0).fire();
        addConnection.setOnAction(event -> {
            Timer tmpTimer = new Timer();
            tmpTimer.set(0, this.connectionMinutes, this.connectionHours);
            for (int i = 0; i < linesMenu.getItems().size(); i++) {
                if (linesMenu.getItems().get(i).equals(linesMenu.getValue())) {
                    if(connectionDirectionButton.getText().equals("Forward")){
                        PTConnection tmpConnection = new PTConnection(mainPubTrans.getLines().get(i), tmpTimer, true, mainMap, mainPubTrans, lineInformationPane, mapCanvas);
                        mainPubTrans.getLines().get(i).addScheduledConnection(tmpConnection);
                    } else {
                        PTConnection tmpConnection = new PTConnection(mainPubTrans.getLines().get(i), tmpTimer, false, mainMap, mainPubTrans, lineInformationPane, mapCanvas);
                        mainPubTrans.getLines().get(i).addScheduledConnection(tmpConnection);
                    }
                    updateScheduledConnectionList(scheduledConnectionList, mainPubTrans.getLines().get(i));
                    break;
                }
            }
        });
        removeConnection.setOnAction(event -> {
            for (int i = 0; i < linesMenu.getItems().size(); i++) {
                if (linesMenu.getItems().get(i).equals(linesMenu.getValue())) {
                    mainPubTrans.getLines().get(i).removeScheduledConnection(this.selectedConnection);
                    updateScheduledConnectionList(scheduledConnectionList, mainPubTrans.getLines().get(i));
                    break;
                }
            }
        });


        // time table generator setup
        generateTimeTable.setOnAction(event ->{
            for (int i = 0; i < linesMenu.getItems().size(); i++) {
                if (linesMenu.getItems().get(i).equals(linesMenu.getValue())) {
                    mainPubTrans.getLines().get(i).generateTimeScheduleToFile("data/generatedTimeTables", mainPubTrans);
                    break;
                }
            }
        });
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
     * Set up of segment menu button for choosing street segments.
     * @param mainMap is the map where the streets are located
     * @param streetMenu is the menu which tells which street is currently selected
     * @param streetSegmentChooser is the menu button to se set
     */
    private void segmentChooserSetter(Map mainMap, ComboBox streetMenu, MenuButton streetSegmentChooser, Slider trafficLevelChooser){
        streetSegmentChooser.getItems().clear();
        for(int i = 0; i < mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getTraffic().size(); i++){
            int finalI = i;
            MenuItem tmpMenuItem = new MenuItem("Segment ".concat(String.valueOf(i)));
            tmpMenuItem.setOnAction(itemEvent -> {
                streetSegmentChooser.setText("Segment ".concat(String.valueOf(finalI)));
                this.selectedSegment = finalI;
                trafficLevelChooser.setValue(mainMap.getStreets().get(mainMap.getMapPointerById((String) streetMenu.getValue())).getTraffic().get(finalI));
            });
            streetSegmentChooser.getItems().add(tmpMenuItem);
            if(i == 0){
                streetSegmentChooser.getItems().get(i).fire();
            }
        }
        MenuItem wholeStreetOption = new MenuItem("Whole street");
        wholeStreetOption.setOnAction(itemEvent -> {
            streetSegmentChooser.setText("Whole street");
            this.selectedSegment = -1;
            trafficLevelChooser.setValue(1);
        });
        streetSegmentChooser.getItems().add(wholeStreetOption);
    }

    private void updateScheduledConnectionList(Accordion list, PTLine line){
        list.getPanes().clear();
        int counter = 0;
        for(int i = 0; i < line.getScheduledConnections().size(); i++){
            counter ++;
            Label tmpText = new Label();
            int finalI = i;
            if(line.getScheduledConnections().get(i).getDepartureTime().getMinutes() < 10){
                tmpText.setText("Departure time: ".concat(String.valueOf(line.getScheduledConnections().get(i).getDepartureTime().getHours()))
                        .concat(":0").concat(String.valueOf(line.getScheduledConnections().get(i).getDepartureTime().getMinutes())));
            } else {
                tmpText.setText("Departure time: ".concat(String.valueOf(line.getScheduledConnections().get(i).getDepartureTime().getHours()))
                        .concat(":").concat(String.valueOf(line.getScheduledConnections().get(i).getDepartureTime().getMinutes())));
            }
            if(line.getScheduledConnections().get(i).getVehicleForward()){
                tmpText.setText(tmpText.getText().concat("\nHeading: forward"));
            } else {
                tmpText.setText(tmpText.getText().concat("\nHeading: backward"));
            }
            TitledPane tmpPane = new TitledPane("Connection ".concat(String.valueOf(i)), tmpText);
            tmpPane.setOnMouseClicked(event -> this.selectedConnection = finalI);
            list.getPanes().add(tmpPane);
        }
        list.setPrefHeight(counter * 27 + 75);
    }

    /**
     * Main function
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
