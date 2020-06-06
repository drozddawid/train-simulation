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
    private StationDatabase stationDatabase = new StationDatabase();


    public static void main (String[] args){
        launch(args);
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
    public void start(Stage primaryStage) {
        primaryStage.setMinHeight(870);
        primaryStage.setMinWidth(900);
        primaryStage.setTitle("Symulacja PKP");
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        map = new Map(stationDatabase);

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

        Label time = new Label("Czas w symulacji: 9:15");
        Label speed = new Label("Prędkość symulacji: 5m/1s");
        Button timetableEdit = new Button("Edycja rozkładu jazdy");

        hBox.setStyle("-fx-background-color: #6699bb; -fx-padding: 0.5em");
        time.setStyle("-fx-background-color: #99ccee; -fx-padding: 0.5em");
        speed.setStyle("-fx-background-color: #99ccee; -fx-padding: 0.5em");

        // A filler is used to make the timetableEdit button appear on the right side
        // [time] [speed] <-------filler--------> [timetableEdit]
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        hBox.getChildren().addAll(time, speed, filler, timetableEdit);
        return hBox;
    }

}
