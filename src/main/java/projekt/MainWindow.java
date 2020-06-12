package projekt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainWindow extends Application{
    private Map map;
    private Timer advancingTimeTimer;
    private TimerTask timerTask;
    private StationDatabase stationDatabase = new StationDatabase();
    private RouteManagerWindow routeManagerWindow = new RouteManagerWindow(new RouteManager(stationDatabase), stationDatabase, this);
    private Button timetableEdit;
    Scene mainScene, routeManagerScene;
    Stage mainWindow;

    public static void main (String[] args){ launch(args);
    }

    private void startAdvancingTimeTimer() {

        /*TimerTask */timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> map.advanceTime());
            }
        };
        
        if(advancingTimeTimer != null)
            advancingTimeTimer.cancel();

        advancingTimeTimer = new Timer();
        advancingTimeTimer.scheduleAtFixedRate(timerTask, 0, 1000/30);
    }

    private void stopAdvancingTimeTimer(){
        advancingTimeTimer.cancel();
        timerTask.cancel();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMinHeight(870);
        primaryStage.setMinWidth(900);
        primaryStage.setTitle("Symulacja PKP");
        primaryStage.setOnCloseRequest(e -> System.exit(0));


        map = new Map(stationDatabase);

        BorderPane root = new BorderPane();
        root.setBottom(bottomHBox());
        root.setCenter(map.getCanvas());

        primaryStage.setScene( mainScene = new Scene(root, 900, 850));
        primaryStage.show();

        routeManagerScene = routeManagerWindow.getScene();
        mainWindow = primaryStage;
        startAdvancingTimeTimer();

        // TODO: delete
        //startSimulating();
    }

    private HBox bottomHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        Label time = new Label("Czas w symulacji: 9:15");
        Label speed = new Label("Prędkość symulacji: " + Settings.getSimulationSpeedMultiplier()/60 + "min/1s");
        TextField setSimulationSpeed = new TextField();
        setSimulationSpeed.setPrefColumnCount(3);
        setSimulationSpeed.setPromptText("Prędkość symulacji (x min/1s)");
        setSimulationSpeed.setOnAction(e -> {
            int speedGiven;
            try{
                speedGiven = Integer.parseInt(setSimulationSpeed.getCharacters().toString());
                if(speedGiven < 0){
                    speedGiven *= -1;
                } if (speedGiven == 0){
                    Settings.setSimulationSpeedMultiplier(1);
                    speed.setText("Prędkość symulacji: czas rzeczywisty (1s/1s)");
                }else if(speedGiven < 121){
                    Settings.setSimulationSpeedMultiplier(speedGiven*60);
                    speed.setText("Prędkość symulacji: " + Settings.getSimulationSpeedMultiplier()/60 + "min/1s");
                }else {
                    Settings.setSimulationSpeedMultiplier(7201);
                    speed.setText("Prędkość symulacji: " + Settings.getSimulationSpeedMultiplier() / 60 + "min/1s");
                }
            }catch (NumberFormatException f){
                routeManagerWindow.showError("Wprowadzono niepoprawną wartość. Wprowadź liczbę całkowitą.");
            }
        });

        timetableEdit = new Button("Ustawienia");
        timetableEdit.setOnAction(e -> {
            mainWindow.setScene(routeManagerScene);
            for(Train train : routeManagerWindow.routeManager.getTrains().values()){
                train.resetRoute();
            }
            stopAdvancingTimeTimer();
            StatisticsLogger.reset();
            map.clearObjects();
        });

        hBox.setStyle("-fx-background-color: #6699bb; -fx-padding: 0.5em");
        time.setStyle("-fx-background-color: #99ccee; -fx-padding: 0.5em");
        speed.setStyle("-fx-background-color: #99ccee; -fx-padding: 0.5em");

        // A filler is used to make the timetableEdit button appear on the right side
        // [time] [speed] <-------filler--------> [timetableEdit]
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        hBox.getChildren().addAll(time, speed,setSimulationSpeed, filler, timetableEdit);
        return hBox;
    }
    public void refreshTrainTab(Tab trainTab){
        ArrayList<Tab> tabs = routeManagerWindow.getRouteManagerTabs();
        TabPane pane = new TabPane();
        pane.getTabs().addAll(tabs.get(0), trainTab, tabs.get(1));
        routeManagerScene = new Scene(pane, 900,850);
    }

    public void startSimulating(){
        startAdvancingTimeTimer();
        mainWindow.setScene(mainScene);
        map.clearObjects();
        map.addObjects(stationDatabase, routeManagerWindow.getRouteManager());
        StatisticsLogger.setMoney(Settings.getInitialMoneyAmount());
    }

    public void changeSettingsButtonText(String text){ timetableEdit.setText(text);}


}
