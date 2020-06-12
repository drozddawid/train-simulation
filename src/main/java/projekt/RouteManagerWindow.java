package projekt;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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

    /**
     *
     * @return a JavaFX Scene representing the window of a RouteManager
     */
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
            Label id = new Label("ID: " + Integer.toString(train.getTrainID()) + ".");
            idBox.getChildren().add(id);
            singleTrain.add(idBox);
            //name
            HBox nameEditor = new HBox(10);
            Label name = new Label("Nazwa: " + train.getName());
            name.setMinWidth(300);
            TextField editNameField = new TextField();
            editNameField.setPrefColumnCount(18);
            editNameField.setOnAction(e -> {
                        train.setName(editNameField.getCharacters().toString());
                        name.setText("Nazwa: " + train.getName());
                        editNameField.setText("");
            });
            nameEditor.getChildren().addAll(name,editNameField);
            singleTrain.add(nameEditor);
            //current route
            String stops = "";
            Label routeShower = new Label();
            HBox routeEditor = new HBox(10);
            Label currentRouteID = new Label("ID trasy: " + train.getCurrentTrainRoute().routeID);
            currentRouteID.setMinWidth(300);
            TextField editCurrentRouteID = new TextField();
            editCurrentRouteID.setPrefColumnCount(4);
            editCurrentRouteID.setOnAction(e -> {
                int identificator;
                try{
                    identificator = Integer.parseInt(editCurrentRouteID.getCharacters().toString());
                    if(routeManager.getRoutes().containsKey(identificator)) {
                        train.resetRoute(routeManager.getRouteByID(Integer.parseInt(editCurrentRouteID.getCharacters().toString())));
                        currentRouteID.setText( "ID trasy: " + train.getCurrentTrainRoute().routeID);
                        String s = "";
                        for(Station stop : train.getCurrentTrainRoute().getStops()){s += stop.name + " - ";}
                        s = s.substring( 0, s.lastIndexOf(" - "));
                        routeShower.setText("Przystanki: " + s);
                        editCurrentRouteID.setText("");

                    } else {
                        this.showError("Nie istnieje trasa o podanym ID: " + identificator);
                    }
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawid\u0142ow\u0105 warto\u015b\u0107.: " + editCurrentRouteID.getCharacters().toString());
                }
            });
            routeEditor.getChildren().addAll(currentRouteID,editCurrentRouteID);
            singleTrain.add(routeEditor);
            //TODO: add showing train route and make it change if user changes it

            for(Station stop : train.getCurrentTrainRoute().getStops()){stops+= stop.name + " - ";}
            stops = stops.substring( 0, stops.lastIndexOf(" - "));
            routeShower.setText("Przystanki: " + stops);
            singleTrain.add(routeShower);
            //seats
            HBox seatsEditor = new HBox(10);
            Label seats = new Label("Ilo\u015b\u0107 siedze\u0144: " + train.getSeats());
            seats.setMinWidth(300);
            TextField editSeats = new TextField();
            editSeats.setPrefColumnCount(4);
            editSeats.setOnAction(e -> {
                int s;
                try{
                    s = Integer.parseInt(editSeats.getCharacters().toString());
                    if(s>0 && s<10001){
                    train.setSeats(s);
                    seats.setText("Ilo\u015b\u0107 siedze\u0144: " + s);
                    editSeats.setText("");
                    } else this.showError("Wprowadzono nieprawid\u0142ow\u0105 warto\u015b\u0107 : " + s + "\n Wprowad\u017a warto\u015b\u0107 z przedzia\u0142u (0;10000>");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawid\u0142ow\u0105 warto\u015b\u0107: " + editSeats.getCharacters().toString());
                }
            });
            seatsEditor.getChildren().addAll(seats,editSeats);
            singleTrain.add(seatsEditor);
            //speed
            HBox speedEditor = new HBox(10);
            Label speed = new Label("Pr\u0119dko\u015b\u0107: " + train.getSpeed() + "km/h");
            speed.setMinWidth(300);
            TextField editSpeed = new TextField();
            editSpeed.setPrefColumnCount(4);
            editSpeed.setOnAction(e -> {
                int s;
                try{
                    s = Integer.parseInt(editSpeed.getCharacters().toString());
                    if(s < 1225) {
                        if (s> 0) {
                            train.setSpeed(s);
                            speed.setText("Pr\u0119dko\u015b\u0107: " + s + "km/h");
                            editSpeed.setText("");
                        } else this.showError("Pr\u0119dko\u015b\u0107 musi by\u0107 wi\u0119ksza od zera.");
                    } else this.showError("Poci\u0105gi nie je\u017cd\u017c\u0105 tak szybko ;)\n Wprowad\u017a warto\u015b\u0107 poni\u017cej pr\u0119dko\u015bci d\u017awi\u0119ku w powietrzu o temperaturze 15 stopni Celsjusza.");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawid\u0142ow\u0105 warto\u015b\u0107: " + editSpeed.getCharacters().toString());
                }
            });
            speedEditor.getChildren().addAll(speed,editSpeed);
            singleTrain.add(speedEditor);
            //costPerKM
            HBox costEditor = new HBox(10);
            Label cost = new Label("Koszt przejazdu jednego kilometra: " + train.getCostPerKM() +"z\u0142.");
            cost.setMinWidth(300);
            TextField editCost = new TextField();
            editCost.setPrefColumnCount(4);
            editCost.setOnAction(e -> {
                int c;
                try{
                    c = Integer.parseInt(editCost.getCharacters().toString());
                    if(c>=0) {
                        train.setCostPerKM(c);
                        cost.setText("Koszt przejazdu jednego kilometra: " + c +"z\u0142.");
                        editCost.setText("");
                    } else this.showError("Koszt nie mo\u017ce by\u0107 ujemny.");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawid\u0142ow\u0105 warto\u015b\u0107: " + editCost.getCharacters().toString());
                }
            });
            costEditor.getChildren().addAll(cost,editCost);
            singleTrain.add(costEditor);
            //profitPerPassenger
            HBox profitEditor = new HBox(10);
            Label profit = new Label("Zysk z jednego pasa\u017cera na kilometr trasy: " + train.getProfitPerPassenger() +"z\u0142");
            profit.setMinWidth(300);
            TextField editProfit = new TextField();
            editProfit.setPrefColumnCount(4);
            editProfit.setOnAction(e -> {
                double s;
                try{
                    s = Double.parseDouble(editProfit.getCharacters().toString());
                    if(s>=0) {
                        train.setProfitPerPassenger(s);
                        profit.setText("Zysk z jednego pasa\u017cera na kilometr trasy: " + s +"z\u0142");
                        editProfit.setText("");
                    } else this.showError("Zysk nie mo\u017ce by\u0107 ujemny.");
                }catch (NumberFormatException ex) {
                    this.showError("Wprowadzono nieprawid\u0142ow\u0105 warto\u015b\u0107: " + editProfit.getCharacters().toString());
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

        Button startSimulating = new Button("Rozpocznij symulacj\u0119");
        startSimulating.setMinWidth(600);
        startSimulating.setAlignment(Pos.TOP_CENTER);
        startSimulating.setOnAction(e -> this.close());
        box.getChildren().add(startSimulating);

        Button addTrain = new Button("Dodaj poci\u0105g");
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
        Tab trains = new Tab("Poci\u0105gi", content);


        settings.getTabs().add(trains);
        return new Scene(settings, 900,850);
    }

    private void close (){ mainWindow.startSimulating();}
    private void showError(String message){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("B\u0142\u0105d");

        Button ok = new Button ("Zamknij");
        ok.setOnAction(f -> dialogStage.close());
        VBox vbox = new VBox(new Label (message), ok);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();

    }


}
