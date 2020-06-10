package projekt;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RouteManagerWindow {
    public RouteManager routeManager;
    public StationDatabase stationDatabase;
    private MainWindow mainWindow;

    RouteManagerWindow (RouteManager routeManager, StationDatabase stationDatabase, MainWindow mainWindow){
        this.routeManager = routeManager;
        this.stationDatabase = stationDatabase;
        this.mainWindow = mainWindow;
    }

    public RouteManager getRouteManager(){ return routeManager;}

    public Scene getScene(){

        TabPane settings = null;
        try {
            settings = (TabPane) FXMLLoader.load(getClass().getResource("/Settings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<Region>> trainsControl = new ArrayList<>();

        for(Train train : routeManager.getTrainsArrayList()){
            ArrayList<Region> singleTrain = new ArrayList<>();
            //separator
            singleTrain.add(new Separator(Orientation.HORIZONTAL));
            //ID
            HBox idBox = new HBox(10);
            Label id = new Label("ID: " + Integer.toString(train.trainID) + ".");
            idBox.getChildren().add(id);
            singleTrain.add(idBox);
            //name
            HBox nameEditor = new HBox(10);
            Label name = new Label("Nazwa: " + train.name);
            name.setMinWidth(300);
            TextField editNameField = new TextField();
            editNameField.setPrefColumnCount(18);
            editNameField.setOnAction(e -> {
                        train.name = editNameField.getCharacters().toString();
                        name.setText("Nazwa: " + train.name);
                        editNameField.setText("");
            });
            nameEditor.getChildren().addAll(name,editNameField);
            singleTrain.add(nameEditor);
            //current route
            String stops = "";
            Label routeShower = new Label();
            HBox routeEditor = new HBox(10);
            Label currentRouteID = new Label("ID trasy: " + train.currentTrainRoute.routeID);
            currentRouteID.setMinWidth(300);
            TextField editCurrentRouteID = new TextField();
            editCurrentRouteID.setPrefColumnCount(4);
            editCurrentRouteID.setOnAction(e -> {
                int identificator;
                try{
                    identificator = Integer.parseInt(editCurrentRouteID.getCharacters().toString());
                    if(routeManager.getRoutes().containsKey(identificator)) {
                        train.resetRoute(routeManager.getRouteByID(Integer.parseInt(editCurrentRouteID.getCharacters().toString())));
                        currentRouteID.setText( "ID trasy: " + train.currentTrainRoute.routeID);
                        String s = "";
                        for(Station stop : train.currentTrainRoute.getStops()){s += stop.name + " - ";}
                        s = s.substring( 0, s.lastIndexOf(" - "));
                        routeShower.setText("Przystanki: " + s);
                        editCurrentRouteID.setText("");

                    } else {
                        this.showError("Nie istnieje trasa o podanym ID: " + identificator);
                    }
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawidłową wartość.: " + editCurrentRouteID.getCharacters().toString());
                }
            });
            routeEditor.getChildren().addAll(currentRouteID,editCurrentRouteID);
            singleTrain.add(routeEditor);
            //TODO: add showing train route and make it change if user changes it

            for(Station stop : train.currentTrainRoute.getStops()){stops+= stop.name + " - ";}
            stops = stops.substring( 0, stops.lastIndexOf(" - "));
            routeShower.setText("Przystanki: " + stops);
            singleTrain.add(routeShower);
            //seats
            HBox seatsEditor = new HBox(10);
            Label seats = new Label("Ilość siedzeń: " + train.seats);
            seats.setMinWidth(300);
            TextField editSeats = new TextField();
            editSeats.setPrefColumnCount(4);
            editSeats.setOnAction(e -> {
                int s;
                try{
                    s = Integer.parseInt(editSeats.getCharacters().toString());
                    if(s>0 && s<10001){
                    train.seats = s;
                    seats.setText("Ilość siedzeń: " + s);
                    editSeats.setText("");
                    } else this.showError("Wprowadzono nieprawidłową wartość : " + s + "\n Wprowadź wartość z przedziału (0;10000>");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawidłową wartość: " + editSeats.getCharacters().toString());
                }
            });
            seatsEditor.getChildren().addAll(seats,editSeats);
            singleTrain.add(seatsEditor);
            //speed
            HBox speedEditor = new HBox(10);
            Label speed = new Label("Prędkość: " + train.speed + "km/h");
            speed.setMinWidth(300);
            TextField editSpeed = new TextField();
            editSpeed.setPrefColumnCount(4);
            editSpeed.setOnAction(e -> {
                int s;
                try{
                    s = Integer.parseInt(editSpeed.getCharacters().toString());
                    if(s < 1225) {
                        if (s> 0) {
                            train.speed = s;
                            speed.setText("Prędkość: " + s + "km/h");
                            editSpeed.setText("");
                        } else this.showError("Prędkość musi być większa od zera.");
                    } else this.showError("Pociągi nie jeżdżą tak szybko ;)\n Wprowadź wartość poniżej prędkości dźwięku w powietrzu o temperaturze 15 stopni Celsjusza.");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawidłową wartość: " + editSpeed.getCharacters().toString());
                }
            });
            speedEditor.getChildren().addAll(speed,editSpeed);
            singleTrain.add(speedEditor);
            //costPerKM
            HBox costEditor = new HBox(10);
            Label cost = new Label("Koszt przejazdu jednego kilometra: " + train.costPerKM +"zł.");
            cost.setMinWidth(300);
            TextField editCost = new TextField();
            editCost.setPrefColumnCount(4);
            editCost.setOnAction(e -> {
                int c;
                try{
                    c = Integer.parseInt(editCost.getCharacters().toString());
                    if(c>=0) {
                        train.costPerKM = c;
                        cost.setText("Koszt przejazdu jednego kilometra: " + c +"zł.");
                        editCost.setText("");
                    } else this.showError("Koszt nie może być ujemny.");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawidłową wartość: " + editCost.getCharacters().toString());
                }
            });
            costEditor.getChildren().addAll(cost,editCost);
            singleTrain.add(costEditor);
            //profitPerPassenger
            HBox profitEditor = new HBox(10);
            Label profit = new Label("Zysk z jednego pasażera na kilometr trasy: " + train.profitPerPassenger +"zł");
            profit.setMinWidth(300);
            TextField editProfit = new TextField();
            editProfit.setPrefColumnCount(4);
            editProfit.setOnAction(e -> {
                double s;
                try{
                    s = Double.parseDouble(editProfit.getCharacters().toString());
                    if(s>=0) {
                        train.profitPerPassenger = s;
                        profit.setText("Zysk z jednego pasażera na kilometr trasy: " + s +"zł");
                        editProfit.setText("");
                    } else this.showError("Zysk nie może być ujemny.");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawidłową wartość: " + editProfit.getCharacters().toString());
                }
            });
            profitEditor.getChildren().addAll(profit,editProfit);
            singleTrain.add(profitEditor);
            for(Region region :singleTrain){
                region.setPadding(new Insets(0,10,0,10));
            }

            trainsControl.add(singleTrain);
        }


        VBox box = new VBox(8);

        Button startSimulating = new Button("Rozpocznij symulację");
        startSimulating.setMinWidth(600);
        startSimulating.setAlignment(Pos.TOP_CENTER);
        startSimulating.setOnAction(e -> this.close());
        box.getChildren().add(startSimulating);

        Button addTrain = new Button("Dodaj pociąg");
        //addTrain.setOnAction(e -> System.out.println(this.trainMaker()));
        box.getChildren().add(addTrain);

        for(ArrayList<Region> singleTrain : trainsControl){
            box.getChildren().addAll(singleTrain);
        }

        box.setSpacing(8);
        box.setVisible(true);
        //box.setAlignment(Pos.TOP_CENTER);
        ScrollPane content = new ScrollPane();
        content.setContent(box);
        content.setFitToHeight(true);
        content.setFitToWidth(true);
        Tab trains = new Tab("Pociągi", content);


        settings.getTabs().add(trains);
        return new Scene(settings, 900,850);
    }

    private void close (){ mainWindow.startSimulating();}
    private void showError(String message){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Błąd");

        Button ok = new Button ("Zamknij");
        ok.setOnAction(f -> dialogStage.close());
        VBox vbox = new VBox(new Label (message), ok);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();

    }

    /*private String trainMaker(){ //this is for testing, later i will make here real bad-ass trainmaker
        Stage trainMaker = new Stage();
        trainMaker.setTitle("Tworzenie pociągu");
        trainMaker.initModality(Modality.WINDOW_MODAL);
        final String[] o = new String[1];
        TextField name = new TextField();
        name.setMinWidth(200);
        Button save = new Button("Save");
        save.setOnAction(e -> {
            o[0] = name.getCharacters().toString();
            trainMaker.close();
        });
        VBox box = new VBox();
        box.getChildren().addAll(name,save);
        box.setPadding(new Insets(30));
        trainMaker.setScene(new Scene(box));
        trainMaker.show();
        return o[0];
    }*/


}
