package projekt;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The window for managing routes and trains
 */
public class RouteManagerWindow {
    public RouteManager routeManager;
    public StationDatabase stationDatabase;
    private MainWindow mainWindow;
    private VBox trainsBox;
    private VBox routesBox;
    private Tab settingsTab;
    private Tab trainsTab;
    private Tab timetableTab;

    RouteManagerWindow (RouteManager routeManager, StationDatabase stationDatabase, MainWindow mainWindow){
        this.routeManager = routeManager;
        this.stationDatabase = stationDatabase;
        this.mainWindow = mainWindow;
        trainsBox = new VBox(8);
        routesBox = new VBox(8);
    }

    public RouteManager getRouteManager(){ return routeManager;}

    /**
     *
     * @return a JavaFX Scene representing the window of a RouteManager
     */
    public Scene getScene(){

        trainsBox.getChildren().clear();
        routesBox.getChildren().clear();
        TabPane settings = new TabPane();
        settings.setMinHeight(850);
        settings.setMinWidth(900);
        settings.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        settings.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
        settingsTab  = getSettingsTab();
        trainsTab = getTrainsTab();
        timetableTab = getTimeTableTab();
        settings.getTabs().addAll(timetableTab, trainsTab, settingsTab);
        Scene routeManagerScene = new Scene(settings, 1400,850);
        return routeManagerScene;
    }

    private ArrayList<Region> getSingleTrainGraphicRepresentation(Train train){
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
                this.showError("Wprowadzono nieprawidłową wartość.: " + editCurrentRouteID.getCharacters().toString());
            }
        });
        routeEditor.getChildren().addAll(currentRouteID,editCurrentRouteID);
        singleTrain.add(routeEditor);

        for(Station stop : train.getCurrentTrainRoute().getStops()){stops+= stop.name + " - ";}
        stops = stops.substring( 0, stops.lastIndexOf(" - "));
        routeShower.setText("Przystanki: " + stops);
        singleTrain.add(routeShower);
        //seats
        HBox seatsEditor = new HBox(10);
        Label seats = new Label("Ilość siedzeń: " + train.getSeats());
        seats.setMinWidth(300);
        TextField editSeats = new TextField();
        editSeats.setPrefColumnCount(4);
        editSeats.setOnAction(e -> {
            int s;
            try{
                s = Integer.parseInt(editSeats.getCharacters().toString());
                if(s>0 && s<10001){
                    train.setSeats(s);
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
        Label speed = new Label("Prędkość: " + train.getSpeed() + "km/h");
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
        Label cost = new Label("Koszt przejazdu jednego kilometra: " + train.getCostPerKM() +"zł.");
        cost.setMinWidth(300);
        TextField editCost = new TextField();
        editCost.setPrefColumnCount(4);
        editCost.setOnAction(e -> {
            int c;
            try{
                c = Integer.parseInt(editCost.getCharacters().toString());
                if(c>=0) {
                    train.setCostPerKM(c);
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
        Label profit = new Label("Zysk z jednego pasażera na kilometr trasy: " + train.getProfitPerPassenger() +"zł");
        profit.setMinWidth(300);
        TextField editProfit = new TextField();
        editProfit.setPrefColumnCount(4);
        editProfit.setOnAction(e -> {
            double s;
            try{
                s = Double.parseDouble(editProfit.getCharacters().toString());
                if(s>=0) {
                    train.setProfitPerPassenger(s);
                    profit.setText("Zysk z jednego pasażera na kilometr trasy: " + s +"zł");
                    editProfit.setText("");
                } else this.showError("Zysk nie może być ujemny.");
            }catch (NumberFormatException ex) {
                this.showError("Wprowadzono nieprawidłową wartość: " + editProfit.getCharacters().toString());
            }
        });
        Button removeTrain = new Button ("Usuń pociąg");
        removeTrain.setOnAction(e-> {
            routeManager.getTrains().remove(train.getTrainID());
            trainsBox.getChildren().removeAll(singleTrain);
        });
        Region filler = new Region();

        profitEditor.getChildren().addAll(profit,editProfit,filler,removeTrain);
        profitEditor.setHgrow(filler, Priority.ALWAYS);
        singleTrain.add(profitEditor);
        for(Region region :singleTrain){
            region.setPadding(new Insets(0,10,0,10));
        }
        return singleTrain;
    }

    private ArrayList<ArrayList<Region>> getTrainsGraphicRepresentation(ArrayList<Train> trains ){
        ArrayList<ArrayList<Region>> trainsControl = new ArrayList<>();

        for(Train train : trains){
            trainsControl.add(getSingleTrainGraphicRepresentation(train));
        }
        return trainsControl;
    }

    private ArrayList<ArrayList<Region>> getRoutesGraphicRepresentation(ArrayList<TrainRoute> routes){
      ArrayList<ArrayList<Region>> routeGraphicRep = new ArrayList<>();
      for(TrainRoute route:routes){
          routeGraphicRep.add(getSingleRouteGraphicRepresentation(route));
      }

      return routeGraphicRep;
    };

    private ArrayList<Region> getSingleRouteGraphicRepresentation(TrainRoute route){
       // VBox routeBox = new VBox(8);
        HBox stationsBox = new HBox(8);
        HBox box = new HBox(8);
        ArrayList<Region> singleRoute = new ArrayList<>();
        singleRoute.add(new Separator(Orientation.HORIZONTAL));
        singleRoute.add(new Label("ID trasy: " + route.routeID));
        ArrayList<Label> stations = new ArrayList<>();
        for(Station station : route.getStops()){
            stations.add(new Label(station.name + "  ") );
        }
        stationsBox.getChildren().addAll(stations);
        box.getChildren().add(stationsBox);
        Button deleteLastStation = new Button ("Usuń ostatnią stację");
        if(stations.size() == 0)deleteLastStation.setText("Usuń trasę");
        AtomicBoolean nextClickDelete = new AtomicBoolean(false);
        deleteLastStation.setOnAction(e -> {
            int i = stations.size();

            if(i == 0){
                nextClickDelete.set(true);
            }if(i == 1) {
                deleteLastStation.setText("Usuń trasę.");
            }else if(i == 2){
                if(routeManager.getTrainsArrayList().size() != 0){
                    for(Train train : routeManager.getTrains().values()){// checking if some trains have empty route, and if it does, changing it to first not empty one found (to avoid problems with no-route trains)
                        if(train.getCurrentTrainRoute().stopsByIDSize == 2){
                        TrainRoute notEmptyRoute = null; //new TrainRoute();
                        for(TrainRoute trainRoute :routeManager.getTrainRoutesArrayList()){
                            if(trainRoute.stopsByIDSize>1){
                                    if(train.getCurrentTrainRoute() != trainRoute) {
                                          notEmptyRoute = trainRoute;
                                          break;
                                    }
                                }
                            }
                            if(notEmptyRoute == null){
                                routeManager.getTrains().clear();
                               //TODO: tell user that his trains will be deleted unless he cancel the action TODO: refresh trains tab
                            }else {
                                train.resetRoute(notEmptyRoute);
                            }

                    }
                    }
                }
            }if(nextClickDelete.get()){
                routesBox.getChildren().removeAll(singleRoute);
                routeManager.getRoutes().remove(route.routeID);
            }else {
                box.getChildren().remove(stations.get(i-1));
                stations.remove(i-1);
                route.getStops().remove(i - 1);
                route.stopsByIDSize--;
            }
            if(routeManager.getTrainRoutesArrayList().size()==0){
                routeManager.getTrains().clear();
            }

            mainWindow.refreshScene(getScene());
        });
        box.getChildren().add(deleteLastStation);

        TextField addStation = new TextField();
        addStation.setPrefColumnCount(50);
        String prompt = "";
        for(Station station : route.getStop(route.stopsByIDSize-1).connectedWith){
            prompt += station.stationID + ": " + station.name + "  ";
        }
        addStation.setPromptText(prompt);
        addStation.setOnAction(e -> {
            if(addStation.getCharacters().length() != 0) {
                int id;
                String stationName;
                try {
                    id = Integer.parseInt(addStation.getCharacters().toString());
                    if (stationDatabase.getStationsById().containsKey(id)) {
                        Station addedStation = stationDatabase.findStation(id);
                        if(stations.size()==0){
                            Label stationLabel = new Label(addedStation.name + "  ");
                            stations.add(stationLabel);
                            stationsBox.getChildren().add(stationLabel);
                            route.getStops().add(addedStation);
                            route.stopsByIDSize += 1;
                            addStation.setText("");
                            String promptTwo = "";
                            for(Station station : route.getStop(route.stopsByIDSize-1).connectedWith){
                                promptTwo += station.stationID + ": " + station.name + "  ";
                            }
                            addStation.setPromptText(promptTwo);
                        }else if (route.getStop(route.stopsByIDSize - 1).connectedWith.contains(addedStation)) {
                            Label stationLabel = new Label(addedStation.name + "  ");
                            stations.add(stationLabel);
                            stationsBox.getChildren().add(stationLabel);
                            route.getStops().add(addedStation);
                            route.stopsByIDSize += 1;
                            addStation.setText("");
                            String promptTwo = "";
                            for(Station station : route.getStop(route.stopsByIDSize-1).connectedWith){
                                promptTwo += station.stationID + ": " + station.name + "  ";
                            }
                            addStation.setPromptText(promptTwo);
                        } else {
                            showError("Podana stacja: " + addedStation.name + " nie jest po\u0142\u0105czona z ostatni\u0105 stacj\u0105 tej trasy: " + route.getStop(route.stopsByIDSize - 1).name + ".");
                        }
                    } else {
                        showError("Stacja o podanym ID: " + id + " nie istnieje.");
                    }
                } catch (NumberFormatException g) {
                    stationName = addStation.getCharacters().toString();

                    if (stationDatabase.getStationsByName().containsKey(stationName)) {
                        Station addedStation = stationDatabase.findStation(stationName);
                        if(stations.size()==0){
                            Label stationLabel = new Label(addedStation.name + "  ");
                            stations.add(stationLabel);
                            stationsBox.getChildren().add(stationLabel);
                            route.getStops().add(addedStation);
                            route.stopsByIDSize += 1;
                            addStation.setText("");
                            String promptTwo = "";
                            for(Station station : route.getStop(route.stopsByIDSize-1).connectedWith){
                                promptTwo += station.stationID + ": " + station.name + "  ";
                            }
                            addStation.setPromptText(promptTwo);
                        }else if (route.getStop(route.stopsByIDSize - 1).connectedWith.contains(addedStation)) {
                            Label stationLabel = new Label(addedStation.name + "  ");
                            stations.add(stationLabel);
                            stationsBox.getChildren().add(stationLabel);
                            route.getStops().add(addedStation);
                            route.stopsByIDSize += 1;
                            addStation.setText("");
                            String promptTwo = "";
                            for(Station station : route.getStop(route.stopsByIDSize-1).connectedWith){
                                promptTwo += station.stationID + ": " + station.name + "  ";
                            }
                            addStation.setPromptText(promptTwo);
                        } else {
                            showError("Podana stacja: " + addedStation.name + " nie jest po\u0142\u0105czona z ostatni\u0105 stacj\u0105 tej trasy: " + route.getStop(route.stopsByIDSize - 1).name + ".");
                        }
                    } else {
                        showError("Stacja o podanej nazwie: " + stationName + " nie istnieje.");
                    }
                }
            } else showError("Nie wprowadzono warto\u015bci.");
        });
        box.getChildren().add(addStation);
        singleRoute.add(box);
        return singleRoute;
    }

    private Tab getTimeTableTab(){
        Button startSimulating = new Button("Rozpocznij symulację");
        startSimulating.setMinWidth(600);
        startSimulating.setAlignment(Pos.TOP_CENTER);
        startSimulating.setOnAction(e -> {
            this.close();
            mainWindow.changeSettingsButtonText("Zatrzymaj symulację i przejdź do ustawień.");
            mainWindow.startSimulating();
        });
        routesBox.getChildren().add(startSimulating);
        Button addTrainRoute = new Button("Dodaj nową trasę");
        addTrainRoute.setOnAction(e->{
            int id = -1;
            for(TrainRoute route : routeManager.getRoutes().values()){
                if(id == -1) id = route.routeID;
                if(route.routeID > id) id = route.routeID;
            }
            id++;
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(1);
            TrainRoute trainRoute = new TrainRoute(id, list , stationDatabase );
            routeManager.getRoutes().put(id,trainRoute);
            routesBox.getChildren().addAll(getSingleRouteGraphicRepresentation(trainRoute));
        });
        routesBox.getChildren().add(addTrainRoute);
        Label advice = new Label ("Aby dodać kolejną stację, wpisz ID lub nazwę i zatwierdź(Enter)\n Aby usunąć trasę, usuń wszystkie stacje.");
        routesBox.getChildren().add(advice);

        for(ArrayList<Region> control : getRoutesGraphicRepresentation(routeManager.getTrainRoutesArrayList())){
            routesBox.getChildren().addAll(control);
        }

        routesBox.setVisible(true);
        ScrollPane content = new ScrollPane();
        content.setContent(routesBox);
        content.setFitToHeight(true);
        content.setFitToWidth(true);
        Tab timetable = new Tab("Trasy", content);

        return timetable;
    }


    private Tab getSettingsTab(){
        VBox box = new VBox();
        Insets padding = new Insets(8);
        Button startSimulating = new Button("Rozpocznij symulację");
        startSimulating.setMinWidth(600);
        startSimulating.setAlignment(Pos.TOP_CENTER);
        startSimulating.setOnAction(e -> {
            this.close();
            mainWindow.changeSettingsButtonText("Zatrzymaj symulację i przejdź do ustawień.");
            mainWindow.startSimulating();
        });
        box.getChildren().add(startSimulating);

        Label initialMoneyAmount = new Label("Początkowa ilość pieniędzy: " + Settings.getInitialMoneyAmount() + "zł");
        TextField setInitialMoneyAmount = new TextField();
        setInitialMoneyAmount.setPrefColumnCount(10);
        setInitialMoneyAmount.setOnAction(e -> {
            try{
                int i = Integer.parseInt(setInitialMoneyAmount.getCharacters().toString());
                Settings.setInitialMoneyAmount(i);
                initialMoneyAmount.setText("Początkowa ilość pieniędzy: " + Settings.getInitialMoneyAmount() + "zł");
            }catch (NumberFormatException exce){
                showError("Wprowadzono nieprawidłową wartość: " + setInitialMoneyAmount.getCharacters().toString());
            }
        });
        HBox moneyEditor = new HBox();
        moneyEditor.setPadding(padding);
        moneyEditor.getChildren().addAll(initialMoneyAmount,setInitialMoneyAmount);

        Label stopCondition = new Label("Czas symulowania: " + Settings.getStopCondition() + "h");
        TextField setStopCondition = new TextField();
        setStopCondition.setPrefColumnCount(10);
        setStopCondition.setOnAction(e -> {
            try{
                int i = Integer.parseInt(setStopCondition.getCharacters().toString());
                Settings.setStopCondition(i);
                stopCondition.setText("Czas symulowania: " + Settings.getStopCondition() + "h");
            }catch (NumberFormatException exce){
                showError("Wprowadzono nieprawidłową wartość: " + setStopCondition.getCharacters().toString());
            }
        });
        HBox stopConditionEditor = new HBox();
        stopConditionEditor.setPadding(padding);
        stopConditionEditor.getChildren().addAll(stopCondition,setStopCondition);

        Label useRouteManager = new Label("Czy używać menedżera tras?\n\nZaznacz jeśli chcesz aby pociągi jeździły według rozkładu." +
                "\nW przeciwnym wypadku trasy będą pseudolosowe.");
        CheckBox ifuseRouteManager = new CheckBox();
        ifuseRouteManager.setSelected(Settings.useRouteManager);
        ifuseRouteManager.setOnAction(e ->{
            boolean b = !Settings.useRouteManager;
            Settings.useRouteManager = b;
            ifuseRouteManager.setSelected(Settings.useRouteManager);
        });
        HBox usingRouteManagerEditor = new HBox();
        usingRouteManagerEditor.setPadding(new Insets(8));
        usingRouteManagerEditor.getChildren().addAll(useRouteManager,ifuseRouteManager);

        box.getChildren().addAll(moneyEditor,stopConditionEditor, usingRouteManagerEditor);
        Tab settings = new Tab("Ustawienia", box);
        return settings;
    }

    private Tab getTrainsTab(){
        Button startSimulating = new Button("Rozpocznij symulację");
        startSimulating.setMinWidth(600);
        startSimulating.setAlignment(Pos.TOP_CENTER);
        startSimulating.setOnAction(e -> {
            this.close();
            mainWindow.changeSettingsButtonText("Zatrzymaj symulację i przejdź do ustawień.");
            mainWindow.startSimulating();
        });
        trainsBox.getChildren().add(startSimulating);

        Button addTrain = new Button("Dodaj pociąg");
        addTrain.setOnAction(e -> {
            try{
                Train newTrain = this.trainMaker();
                routeManager.getTrains().put(newTrain.getTrainID(), newTrain);
                trainsBox.getChildren().addAll(getSingleTrainGraphicRepresentation(newTrain));

            } catch(NullPointerException exc){
                showError("Dodawanie pociągu anulowane.");
            }
        });
        trainsBox.getChildren().add(addTrain);

        for(ArrayList<Region> singleTrain : getTrainsGraphicRepresentation(routeManager.getTrainsArrayList())){
            trainsBox.getChildren().addAll(singleTrain);
        }

        trainsBox.setSpacing(8);
        trainsBox.setVisible(true);
        //box.setAlignment(Pos.TOP_CENTER);
        ScrollPane content = new ScrollPane();
        content.setContent(trainsBox);
        content.setFitToHeight(true);
        content.setFitToWidth(true);
        Tab trains = new Tab("Pociągi", content);
        return trains;
    }

    private void close (){ mainWindow.startSimulating();}
    public void showError(String message){
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

    public void showStats(String stats){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Symulacja została zakończona.");

        Button ok = new Button ("Zamknij");
        ok.setOnAction(f -> dialogStage.close());
        VBox vbox = new VBox(new Label (stats), ok);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();

    }

    public ArrayList<Tab> getRouteManagerTabs(){
        ArrayList<Tab> list = new ArrayList<>();
        list.add(settingsTab);
        list.add(timetableTab);
        return list;
    }

    private Train trainMaker(){
        Stage trainMaker = new Stage();
        trainMaker.setTitle("Tworzenie pociągu");
        trainMaker.setMinHeight(400);
        trainMaker.setMinWidth(800);
        trainMaker.initModality(Modality.WINDOW_MODAL);
        final Train[] train = new Train[1];

        //name
        TextField name = new TextField();
        name.setPromptText("Nazwa");
        name.setMinWidth(200);
        //currentRoute
        TextField currentRouteID = new TextField();
        currentRouteID.setPromptText("ID trasy");
        currentRouteID.setMinWidth(200);
        Label routeShower = new Label("Wciśnij enter aby wyświetlić przystanki");
        currentRouteID.setOnAction(e -> {
            int routeIDGiven;
            try{
                routeIDGiven = Integer.parseInt(currentRouteID.getCharacters().toString());
                if(routeManager.getRoutes().containsKey(routeIDGiven)) {
                    String s = "";
                    for(Station stop : routeManager.getRouteByID(routeIDGiven).getStops()){s += stop.name + " - ";}
                    s = s.substring( 0, s.lastIndexOf(" - "));
                    routeShower.setText("Przystanki: " + s);

                } else {
                    this.showError("Nie istnieje trasa o podanym ID: " + routeIDGiven);
                }
            }catch (NumberFormatException ex) {
                this.showError("Wprowadzono nieprawidłową wartość.: " + currentRouteID.getCharacters().toString());
            }
        });
        //seats
        TextField seats = new TextField();
        seats.setPromptText("Ilość siedzeń");
        seats.setMinWidth(200);
        //speed
        TextField speed = new TextField();
        speed.setPromptText("Prędkość (km/h)");
        speed.setMinWidth(200);
        //costPerKM
        TextField costPerKM = new TextField();
        costPerKM.setPromptText("Koszt przejazdu jednego kilometra");
        costPerKM.setMinWidth(200);
        //profitPerPassenger
        TextField profitPerPassenger = new TextField();
        profitPerPassenger.setPromptText("Zysk z przewiezienia jednego klienta na trasie o długości jednego kilometra");
        profitPerPassenger.setMinWidth(200);

        final int[] id = {-1};
        final String[] nameGiven = {""};
        final String[] errorMessages = {""};
        final boolean[] errorOccured = {false};
        final TrainRoute[] routeGiven = new TrainRoute[]{new TrainRoute()};
        final int[] seatsGiven = {0};
        final int[] speedGiven = {0};
        final int[] costGiven = {0};
        final double[] profitGiven = {0};

        Button save = new Button("Dodaj pociąg");
        save.setOnAction(e -> {

            //getting last train made ID
            for(Train currentEntry : routeManager.getTrains().values()){
                if(id[0] == -1)  id[0] = currentEntry.getTrainID();
                if(currentEntry.getTrainID() > id[0]) id[0] = currentEntry.getTrainID();
            }
            //new train id
            if (id[0] == -1) {
                id[0] = 1;
            }else id[0]++;
            //getting name

            if (name.getCharacters().length() != 0) {
                nameGiven[0] = name.getCharacters().toString();
            } else {
                errorMessages[0] += "\nNie podano nazwy."; errorOccured[0] = true;};

            //getting currentTrainRoute
            int routeIDGiven;
            if(currentRouteID.getCharacters().length() != 0){
                try{
                    routeIDGiven = Integer.parseInt(currentRouteID.getCharacters().toString());
                    if(routeManager.getRoutes().containsKey(routeIDGiven) && routeManager.getRouteByID(routeIDGiven).stopsByIDSize > 1) {
                    routeGiven[0] = routeManager.getRouteByID(routeIDGiven);
                    } else {
                        errorMessages[0] += "\nNie istnieje trasa o podanym ID: " + routeIDGiven;
                        errorOccured[0] = true;
                    }
                }catch (NumberFormatException ex) {
                    errorMessages[0] += "\nID trasy: Wprowadzono nieprawid\u0142ow\u0105 warto\u015b\u0107.: " + currentRouteID.getCharacters().toString();
                    errorOccured[0] = true;
                }
            }else {
                errorMessages[0] += "\nNie wprowadzono ID trasy.";
                errorOccured[0] = true;
            }

            //getting seats

            if(seats.getCharacters().length() != 0){
                int s;
                try{
                    s = Integer.parseInt(seats.getCharacters().toString());
                    if(s>0 && s<10001){
                        seatsGiven[0] = s;
                    } else {
                        errorMessages[0] += "\nIlość siedzeń: Wprowadzono nieprawidłową wartość : " + s + " Wprowadź wartość z przedziału (0;10000>";
                        errorOccured[0] = true;
                    }
                }catch (NumberFormatException ex) {
                    errorMessages[0] += "\nIlość siedzeń: Wprowadzono nieprawidłową wartość: " + seats.getCharacters().toString();
                    errorOccured[0] = true;
                }
            } else {
                errorMessages[0] += "\nNie wprowadzono liczby siedzeń."; errorOccured[0] =true;}
            //getting speed

            if(speed.getCharacters().length() != 0){
                int s;
                try{
                    s = Integer.parseInt(speed.getCharacters().toString());
                    if(s<1225){
                        if(s>0) {
                            speedGiven[0] = s;
                        } else { errorMessages[0] += "\nPrędkość musi być większa od zera."; errorOccured[0] = true; }
                    } else {
                        errorMessages[0] += "\nPociągi nie jeżdżą tak szybko ;)\nWprowadź wartość prędkości mniejszą niż prędkość dźwięku w powietrzu o temperaturze 15 stopni Celsjusza.";
                        errorOccured[0] = true;
                    }
                }catch (NumberFormatException ex) {
                    errorMessages[0] += "\nPrędkość: Wprowadzono nieprawidłową wartość: " + speed.getCharacters().toString();
                    errorOccured[0] = true;
                }
            } else {
                errorMessages[0] += "\nNie wprowadzono prędkości."; errorOccured[0] = true;}
            //getting cost

            int c;
            if(costPerKM.getCharacters().length() != 0) {
                try {
                    c = Integer.parseInt(costPerKM.getCharacters().toString());
                    if (c >= 0) {
                        costGiven[0] = c;
                    } else {
                        errorMessages[0] += "\nKoszt nie może być ujemny."; errorOccured[0] = true;}
                } catch (NumberFormatException ex) {
                    errorMessages[0] += "\nKoszt przejazdu jednego kilometra: Wprowadzono nieprawidłową wartość: " + costPerKM.getCharacters().toString();
                    errorOccured[0] = true;
                }
            } else {
                errorMessages[0] += "\nNie wprowadzono kosztu przejazdu jednego kilometra."; errorOccured[0] = true;}
            //getting profit

            double s;
            if(profitPerPassenger.getCharacters().length() != 0) {
                try {
                    s = Double.parseDouble(profitPerPassenger.getCharacters().toString());
                    if (s >= 0) {
                        profitGiven[0] = s;
                    } else {
                        errorMessages[0] += "\nZysk nie może być ujemny.";
                        errorOccured[0] = true;
                    }
                } catch (NumberFormatException ex) {
                    errorMessages[0] += "\nZysk z przewozu jednego pasażera na trasie o długości kilometra: Wprowadzono nieprawidłową wartość: " + profitPerPassenger.getCharacters().toString();
                    errorOccured[0] = true;
                }
            } else {errorMessages[0] += "\nNie wprowadzono zysku z przewozu jednego pasażera na trasie o długości kilometra."; errorOccured[0] = true;}
            for (TrainRoute route :routeManager.getRoutes().values()){
                //sprawdź czy jakakolwiek trasa ma więcej niż jeden przystanek
            }
            if(!errorOccured[0]){
                train[0] = new Train(id[0],nameGiven[0], costGiven[0], profitGiven[0], seatsGiven[0], speedGiven[0], routeGiven[0]);
                trainMaker.close();
            } else {
                showError(errorMessages[0]);
                errorMessages[0] = "";
                errorOccured[0] = false;
            }


        });
        VBox box = new VBox();
        box.getChildren().addAll(name, currentRouteID, routeShower, seats, speed, costPerKM, profitPerPassenger, save);
        box.setPadding(new Insets(30));
        trainMaker.setScene(new Scene(box));
        trainMaker.showAndWait();

        return train[0];
    }

}