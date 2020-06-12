package projekt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main window of the application. Contain the Map and RouteManager as scenes.
 */
public class MainWindow extends Application {
    private Map map;
    private Timer advancingTimeTimer;
    private TimerTask timerTask;
    private StationDatabase stationDatabase = new StationDatabase();
    private RouteManagerWindow routeManagerWindow = new RouteManagerWindow(new RouteManager(stationDatabase), stationDatabase, this);
    private Button timetableEdit;
    private Scene mainScene, routeManagerScene;
    private Stage mainWindow;
    private double time = 0;
    private Label timeLabel;
    private BorderPane root;

    /**
     * The entry function.
     *
     * @param args command-line arguments
     */
    public static void main (String[] args){ launch(args); }


    /**
     * Starts the timer that schedules an update at 30fps
     */
    private void startAdvancingTimeTimer() {

        /*TimerTask */timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    time += 1/30.0*Settings.getSimulationSpeedMultiplier()/60.0;
                    updateTime();
                    map.advanceTime();
                    if(time > Settings.getStopCondition() * 60) {
                        showStats(StatisticsLogger.getEndMessage());
                        advancingTimeTimer.cancel();
                    }
                });
            }
        };
        
        if(advancingTimeTimer != null)
            advancingTimeTimer.cancel();

        advancingTimeTimer = new Timer();
        advancingTimeTimer.scheduleAtFixedRate(timerTask, 0, 1000/30);
    }

    /**
     * The "real" entry function, provided by JavaFX.
     */
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

        root = new BorderPane();
        root.setBottom(bottomHBox());
        root.setCenter(map.getCanvas());

        primaryStage.setScene( mainScene = new Scene(root, 900, 850));
        primaryStage.show();

        routeManagerScene = routeManagerWindow.getScene();
        mainWindow = primaryStage;
        startAdvancingTimeTimer();

        startSimulating();
    }

    private void updateTime() {
        int hour = ((int)time / 60) % 24;
        int minute = (int)time % 60;
        String padding = minute > 9 ? "" : "0";
        Label time = new Label("Czas w symulacji: " + hour + ":" + padding + minute);
        time.setStyle("-fx-background-color: #99ccee; -fx-padding: 0.5em");
        ((HBox)root.getBottom()).getChildren().set(0, time);
    }

    /**
     * Constructs the blue bottom bar on the main screen
     * [time] [speed] <-------filler--------> [timetableEdit]
     *
     * @return the bottom bar.
     */
    public void showStats(String stats){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Symulacja została zakończona.");

        Button ok = new Button ("Zamknij");
        ok.setOnAction(f -> { dialogStage.close(); System.exit(0);});
        VBox vbox = new VBox(new Label (stats), ok);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();

    }
    private HBox bottomHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        int hour = ((int)time / 60) % 24;
        int minute = (int)time % 60;
        Label time = new Label("Czas w symulacji:");
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
    public void refreshScene(Scene scene){
        routeManagerScene = scene;
        mainWindow.setScene(routeManagerScene);

    }


    public void refreshTrainTab(Tab trainTab){
        ArrayList<Tab> tabs = routeManagerWindow.getRouteManagerTabs();
        TabPane pane = new TabPane();
        pane.getTabs().addAll(tabs.get(0), trainTab, tabs.get(1));
        routeManagerScene = new Scene(pane, 900,850);
    }

    /**
     * Starts the simulation when starting the application and when exiting from RouteManagerWindow
     */
    public void startSimulating(){
        time = 0;
        startAdvancingTimeTimer();
        mainWindow.setScene(mainScene);
        map.clearObjects();
        map.addObjects(stationDatabase, routeManagerWindow.getRouteManager());
        StatisticsLogger.setMoney(Settings.getInitialMoneyAmount());
    }

    public void changeSettingsButtonText(String text){ timetableEdit.setText(text);}


}
