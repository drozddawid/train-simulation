package projekt;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.lang.Math.*;

public class Train extends MapObject{

    public int trainID;
    public String name;
    public int costPerKM;
    public double profitPerPassenger; //profit for one passenger per ONE KILOMETER
    public int seats;
    public int passengers;
    public int speed; //km/h
    public Station previousStation;
    public Station nextStation;
    public TrainRoute currentTrainRoute;
    private int currentStopID; //points last stop in which train were
    private boolean routeDirection; // true - train goes from station 0 to last station, false - train goes from last station to station 0 in its currentTrainRoute
    public double linkProgress;
    public double testtime;

    Train(int trainID, String name, int costPerKM, double profitPerPassenger, int seats, int speed, TrainRoute route){
        this.trainID = trainID;
        this.name = name;
        this.costPerKM = costPerKM;
        this.profitPerPassenger = profitPerPassenger;
        this.speed = speed;
        this.seats = seats;

        this.currentTrainRoute = route;
        this.previousStation = this.currentTrainRoute.getStop(0);
        this.nextStation = this.currentTrainRoute.getStop(1);

        this.passengers = 0;
        this.currentStopID = 0;
        this.routeDirection = true;
        this.linkProgress = 0;
        this.testtime = 0;
//        this.coordX = currentLink.from.coordX;
//        this.coordY = currentLink.from.coordY;

    }
    /*public Train(){
        this(0,"defaultTrainName",0,0, 0,0, new TrainRoute());
    }*/

    public String getTrain(){
        return "\nprojekt.Train ID: " + this.trainID + "\nName: " + this.name + "\nCost (per KM): " + this.costPerKM
                + "\nProfit per passenger for KM: " + this.profitPerPassenger + "\nSeats: " + this.seats
                + "\nLink Progress (%): " + (this.linkProgress*100)
                + "\nCoordX: " + this.coordX + "\nCoordY: " + this.coordY;
    }

    @Override
    public void tick() {
        double distanceY = nextStation.coordY - previousStation.coordY;
        double distanceX = nextStation.coordX - previousStation.coordX;
        double distance = Math.hypot(distanceX, distanceY);

        if(linkProgress < 1) {
            linkProgress += this.speed * Settings.vCoefficient * Settings.getSimulationSpeedMultiplier() / distance;
            //linkProgress += this.speed * 0.0000636 * MainWindow.getSimulationSpeedMultiplier() / distance; //TODO: delete this line if previous one is legit
        } else {
            linkProgress = 1.0;
            StatisticsLogger.logArrival(this);
            if(Settings.useRouteManager){
                this.nextStop();
            }else this.nextRndStop();

        }

        coordY = previousStation.coordY + distanceY * linkProgress;
        coordX = previousStation.coordX + distanceX * linkProgress;
    }

    @Override
    public void draw(GraphicsContext gc){
        // The train icon is 314x472 pixels
        final double WIDTH = 314;
        final double HEIGHT = 472;

        gc.beginPath();
        gc.translate(coordX, coordY);
        gc.scale(0.035, 0.035);
        gc.translate(-WIDTH/2, -HEIGHT/2);
        gc.appendSVGPath(ObjectPathResourceGetter.getInstance().getValue("svgPath.train"));
        gc.closePath();
        gc.fill();
    }

    public void drawInfoBox(GraphicsContext gc) {
        final double ICON_EDGE = 11.0;
        final double ICON_RADIUS = Math.hypot(ICON_EDGE / 2, ICON_EDGE / 2);
        gc.strokeOval(coordX - ICON_EDGE, coordY - ICON_EDGE, 2 * ICON_EDGE, 2 * ICON_EDGE);

//        boolean onTheRight = coordX < gc.getCanvas().getWidth() / 2;
//        boolean onTheBottom = coordY < gc.getCanvas().getHeight() / 2;

        ArrayList<String> lines = new ArrayList<>();
        lines.add("Pociąg: " + name);
        if(previousStation != null) {
            lines.add("Poprzednia stacja: " + previousStation.name);
        }
        if(nextStation != null) {
            lines.add("Następna stacja: " + nextStation.name);
        }

        ArrayList<Station> stops = currentTrainRoute.getStops();
        if(stops != null && stops.size() > 1) {
            lines.add("Relacja:");
            for(Station station : stops) {
                lines.add("     - " + station.name);
            }
        }

        final double BORDER = 3;
        final double FONT_HEIGHT = 11;
        final double BOX_WIDTH = 200;
        final double BOX_HEIGHT = BORDER * 2 + (FONT_HEIGHT + 1) * lines.size();
        double boxX = coordX - BOX_WIDTH - ICON_RADIUS;
        double boxY = coordY - BOX_HEIGHT - ICON_RADIUS;

        gc.rect(boxX, boxY, BOX_WIDTH, BOX_HEIGHT);
        gc.closePath();
        gc.setFill(Paint.valueOf("#ffffff"));
        gc.setStroke(Paint.valueOf("#000000"));
        gc.fill();
        gc.stroke();
        gc.setFill(Paint.valueOf("#000000"));
        gc.setTextBaseline(VPos.TOP);
        gc.setFont(new Font(FONT_HEIGHT));

        int lineNumber = 0;
        for(String line : lines) {
            double textX = boxX + BORDER;
            double textY = boxY + BORDER + lineNumber * (FONT_HEIGHT + 1);
            gc.fillText(line, textX, textY, BOX_WIDTH - BORDER * 2);
            lineNumber += 1;
        }
    }

    private void nextRndStop (){ // sets next course (randomly) for train (used when it's linkProgress == 1)
        this.previousStation = this.nextStation;
        int randomStationIndex = (int)(Math.random() *this.previousStation.connectedWith.size());
        this.nextStation = this.previousStation.connectedWith.get(randomStationIndex);
        this.passengers = (int) (Math.random() * this.seats);
        this.linkProgress = 0.0;
        this.testtime = System.currentTimeMillis(); //for testing time of travel between stations to scale train speed (succeeded)
    }

    private void nextStop(){ // sets next course (from currentTrainRoute) for train (used when it's linkProgress == 1)
        if(this.routeDirection && this.currentStopID == this.currentTrainRoute.stopsByIDSize - 2){
            this.routeDirection = false;
            this.currentStopID += 2;
        } else if (!this.routeDirection && currentStopID == 1){
            this.routeDirection = true;
            this.currentStopID -=2;
        }

        if(this.routeDirection) {
            this.currentStopID++;
            this.previousStation = this.currentTrainRoute.getStop(this.currentStopID);
            this.nextStation = this.currentTrainRoute.getStop(this.currentStopID + 1);
        } else{
            this.currentStopID--;
            this.previousStation = this.currentTrainRoute.getStop(this.currentStopID);
            this.nextStation = this.currentTrainRoute.getStop(this.currentStopID - 1);
        }

        this.passengers = (int) (Math.random() * this.seats);
        this.linkProgress = 0.0;
        this.testtime = System.currentTimeMillis();
    }

    public void resetRoute(TrainRoute route){
        this.currentTrainRoute = route;
        this.currentStopID = 0;
        this.routeDirection = true;
        this.previousStation = route.getStop(0);
        this.nextStation = route.getStop(1);
        this.linkProgress = 0;
    }
}
