package projekt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.io.InputStream;
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
    public double linkProgress;
    public double testtime;

    Train(int trainID, String name, int costPerKM, double profitPerPassenger, int seats, int speed/*, StationLink currentLink*/){
        this.trainID = trainID;
        this.name = name;
        this.costPerKM = costPerKM;
        this.profitPerPassenger = profitPerPassenger;
        this.seats = seats;
//        this.currentLink = currentLink;
        this.linkProgress = 0;
        this.passengers = 0;
        this.speed = speed;
        this.testtime = 0;
//        this.coordX = currentLink.from.coordX;
//        this.coordY = currentLink.from.coordY;

    }
    public Train(){
        this(0,"defaultTrainName",0,0, 0,0 );
    }

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
            linkProgress += this.speed * 0.0000636 * MainWindow.getSimulationSpeedMultiplier() / distance;
        } else {
            linkProgress = 1.0;
            StatisticsLogger.logArrival(this);
            this.nextCourse();
        }

        coordY = previousStation.coordY + distanceY * linkProgress;
        coordX = previousStation.coordX + distanceX * linkProgress;
    }

    @Override
    public void draw(GraphicsContext gc){
        // The train icon is 314x472 pixels
        final double WIDTH = 314;
        final double HEIGHT = 472;

        gc.save();
        //gc.setFill(Paint.valueOf("#123456"));
        gc.translate(coordX, coordY);
        gc.scale(0.035, 0.035);
        gc.translate(-WIDTH/2, -HEIGHT/2);
        gc.beginPath();
        //gc.scale(0.05, 0.05);
        gc.appendSVGPath(ObjectPathResourceGetter.getInstance().getValue("svgPath.train"));
        gc.closePath();
        gc.fill();
        gc.restore();
    }

    private void nextCourse (){ // sets next course for train when it's linkProgress == 1
        //idk if this should be in train class but works
        this.previousStation = this.nextStation;
        int randomStationIndex = (int)(Math.random() *this.previousStation.connectedWith.size());
        this.nextStation = this.previousStation.connectedWith.get(randomStationIndex);
        this.passengers = (int) (Math.random() * this.seats);
        this.linkProgress = 0.0;
        this.testtime = System.currentTimeMillis(); //for testing time of travel between stations to scale train speed (succeeded)
    }

}
