package projekt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;


public class MainWindow extends Application {
    private Map map;
    private Timer advancingTimeTimer;

    public static void main (String[] args){
        launch(args);
/*
        RouteTime time = new RouteTime();

        File stationData = new File ("src/stationData.txt");
        File linkData = new File("src/linkData.txt");
        File trainData = new File("src/trainData.txt");
        StationDatabase dataBase = new StationDatabase(stationData, linkData, trainData);
        System.out.println("===========STACJE=========");
        for(int i = 0; i<4;i++) {
            System.out.println(dataBase.stations.get(i).getStation());
        }
        System.out.println("===========POLACZENIA=========");
        for(int i = 0; i<4;i++) {
            System.out.println(dataBase.stationLinks.get(i).getLink());
        }
        System.out.println("===========POCIAGI=========");
        for(int i = 0; i<5;i++) {
            System.out.println(dataBase.trains.get(i).getTrain());
        }
        System.out.println("Minelo " + RouteTime.getTime() + " ms");
        */
    }

    private void startAdvancingTimeTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> map.advanceTime());
            }
        };
        advancingTimeTimer = new Timer();
        advancingTimeTimer.scheduleAtFixedRate(timerTask, 0, 1000/30);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinHeight(870);
        primaryStage.setMinWidth(900);
        primaryStage.setTitle("Hello World!");
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        map = new Map();

        BorderPane root = new BorderPane();
        root.setBottom(bottomHBox());
        root.setCenter(map.getCanvas());

        primaryStage.setScene(new Scene(root, 900, 850));
        primaryStage.show();

        startAdvancingTimeTimer();

    }

    private HBox bottomHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        Label time = new Label("Czas w symulacji: 9:15");
        Label speed = new Label("Prędkość symulacji: 5m/1s");
        Button timetableEdit = new Button("Edycja rozkładu jazdy");

        hBox.setStyle("-fx-background-color: #6699bb; -fx-padding: 0.5em");
        time.setStyle("-fx-background-color: #99ccee; -fx-padding: 0.5em");
        speed.setStyle("-fx-background-color: #99ccee; -fx-padding: 0.5em");
        hBox.getChildren().addAll(time, speed, filler, timetableEdit);
        return hBox;
    }
}
