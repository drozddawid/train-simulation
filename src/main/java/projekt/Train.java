package projekt;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * A train to be drawn on the map
 */
public class Train extends MapObject{
    private int trainID;
    private String name;
    private int costPerKM;
    private double profitPerPassenger; //profit for one passenger per ONE KILOMETER
    private int seats;
    private int passengers;
    private int speed; //km/h
    private Station previousStation;
    private Station nextStation;
    private TrainRoute currentTrainRoute;
    /**
     * to the last stop in which the train was located
     */
    private int currentStopID;
    /**
     * true - train goes from station 0 to last station, false - train goes from last station to station 0 in its currentTrainRoute
     */
    private boolean routeDirection;
    private double linkProgress;
    private double testtime;

    /**
     * Constructs a Train.
     *
     * @param trainID - the numerical id, from 1 to INT_MAX
     * @param name - for example "Mickiewicz"
     * @param costPerKM
     * @param profitPerPassenger
     * @param seats
     * @param speed
     * @param route
     */
    Train(int trainID, String name, int costPerKM, double profitPerPassenger, int seats, int speed, TrainRoute route){
        this.setTrainID(trainID);
        this.setName(name);
        this.setCostPerKM(costPerKM);
        this.setProfitPerPassenger(profitPerPassenger);
        this.setSpeed(speed);
        this.setSeats(seats);

        this.setCurrentTrainRoute(route);
        this.setPreviousStation(this.getCurrentTrainRoute().getStop(0));
        this.setNextStation(this.getCurrentTrainRoute().getStop(1));

        this.setPassengers(0);
        this.currentStopID = 0;
        this.routeDirection = true;
        this.setLinkProgress(0);
        this.setTesttime(0);
    }

    /**
     * Moves the train forward a tiny bit
     */
    @Override
    public void tick() {
        double distanceY = getNextStation().coordY - getPreviousStation().coordY;
        double distanceX = getNextStation().coordX - getPreviousStation().coordX;
        double distance = Math.hypot(distanceX, distanceY);

        if(getLinkProgress() < 1) {
            setLinkProgress(getLinkProgress() + this.getSpeed() * Settings.vCoefficient * Settings.getSimulationSpeedMultiplier() / distance);
            //linkProgress += this.speed * 0.0000636 * MainWindow.getSimulationSpeedMultiplier() / distance; //TODO: delete this line if previous one is legit
        } else {
            setLinkProgress(1.0);
            StatisticsLogger.logArrival(this);
            if(Settings.useRouteManager){
                this.nextStop();
            }else this.nextRndStop();

        }

        coordY = getPreviousStation().coordY + distanceY * getLinkProgress();
        coordX = getPreviousStation().coordX + distanceX * getLinkProgress();
    }

    /**
     * Draws the Train on a Map.
     *
     * @param gc the context on which to draw the object
     */
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

    /**
     * Draws a small infoBox when the mouse pointer is close to the train
     *
     * @param gc the context on which to draw the objectc
     */
    public void drawInfoBox(GraphicsContext gc) {
        final double ICON_EDGE = 11.0;
        final double ICON_RADIUS = Math.hypot(ICON_EDGE / 2, ICON_EDGE / 2);
        gc.strokeOval(coordX - ICON_EDGE, coordY - ICON_EDGE, 2 * ICON_EDGE, 2 * ICON_EDGE);

//        boolean onTheRight = coordX < gc.getCanvas().getWidth() / 2;
//        boolean onTheBottom = coordY < gc.getCanvas().getHeight() / 2;

        ArrayList<String> lines = new ArrayList<>();
        lines.add("Poci\u0105g: " + getName());
        if(getPreviousStation() != null) {
            lines.add("Poprzednia stacja: " + getPreviousStation().name);
        }
        if(getNextStation() != null) {
            lines.add("Nast\u0119pna stacja: " + getNextStation().name);
        }

        ArrayList<Station> stops = getCurrentTrainRoute().getStops();
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

    /**
     * Random mode: sets next course (randomly) for train (used when it's linkProgress >= 1)
     */
    private void nextRndStop (){
        this.setPreviousStation(this.getNextStation());
        int randomStationIndex = (int)(Math.random() * this.getPreviousStation().connectedWith.size());
        this.setNextStation(this.getPreviousStation().connectedWith.get(randomStationIndex));
        this.setPassengers((int) (Math.random() * this.getSeats()));
        this.setLinkProgress(0.0);
        this.setTesttime(System.currentTimeMillis()); //for testing time of travel between stations to scale train speed (succeeded)
    }

    /**
     * Timetable mode: sets next course (from currentTrainRoute) for train (used when it's linkProgress >= 1)
     */
    private void nextStop(){
        if(this.routeDirection && this.currentStopID == this.getCurrentTrainRoute().stopsByIDSize - 2){
            this.routeDirection = false;
            this.currentStopID += 2;
        } else if (!this.routeDirection && currentStopID == 1){
            this.routeDirection = true;
            this.currentStopID -=2;
        }

        if(this.routeDirection) {
            this.currentStopID++;
            this.setPreviousStation(this.getCurrentTrainRoute().getStop(this.currentStopID));
            this.setNextStation(this.getCurrentTrainRoute().getStop(this.currentStopID + 1));
        } else{
            this.currentStopID--;
            this.setPreviousStation(this.getCurrentTrainRoute().getStop(this.currentStopID));
            this.setNextStation(this.getCurrentTrainRoute().getStop(this.currentStopID - 1));
        }

        this.setPassengers((int) (Math.random() * this.getSeats()));
        this.setLinkProgress(0.0);
        this.setTesttime(System.currentTimeMillis());
    }

    /**
     * Resets the train and places it on the beggining of a TrainRoute
     *
     * @param route the TrainRoute being reset
     */
    public void resetRoute(TrainRoute route){
        this.setCurrentTrainRoute(route);
        this.currentStopID = 0;
        this.routeDirection = true;
        this.setPreviousStation(route.getStop(0));
        this.setNextStation(route.getStop(1));
        this.setLinkProgress(0);
    }

    public int getTrainID() {
        return trainID;
    }

    public void setTrainID(int trainID) {
        this.trainID = trainID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCostPerKM() {
        return costPerKM;
    }

    public void setCostPerKM(int costPerKM) {
        this.costPerKM = costPerKM;
    }

    public double getProfitPerPassenger() {
        return profitPerPassenger;
    }

    public void setProfitPerPassenger(double profitPerPassenger) {
        this.profitPerPassenger = profitPerPassenger;
    }

    /**
     *
     * @return the number of seats
     */
    public int getSeats() {
        return seats;
    }

    /**
     *
     * @param seats the number of seats
     */
    public void setSeats(int seats) {
        this.seats = seats;
    }

    /**
     *
     * @return the number of passengers
     */
    public int getPassengers() {
        return passengers;
    }

    /**
     *
     * @param passengers the number of passengers
     */
    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Station getPreviousStation() {
        return previousStation;
    }

    public void setPreviousStation(Station previousStation) {
        this.previousStation = previousStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public void setNextStation(Station nextStation) {
        this.nextStation = nextStation;
    }

    public TrainRoute getCurrentTrainRoute() {
        return currentTrainRoute;
    }

    public void setCurrentTrainRoute(TrainRoute currentTrainRoute) {
        this.currentTrainRoute = currentTrainRoute;
    }

    /**
     * @return how much between previousStation and nextStation the Train has moved - between [0.0; 1.0]
     */
    public double getLinkProgress() {
        return linkProgress;
    }

    /**
     *
     * @param linkProgress how much between previousStation and nextStation the Train has moved - between [0.0; 1.0]
     */
    public void setLinkProgress(double linkProgress) {
        this.linkProgress = linkProgress;
    }

    public double getTesttime() {
        return testtime;
    }

    public void setTesttime(double testtime) {
        this.testtime = testtime;
    }
}
