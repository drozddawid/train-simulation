package projekt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StatisticsLogger {//TODO:(make it show in window and save to datafile) for now it just shows in console what statisticlogger should show on panel and save to some datafile
    private static int money = 0;
    private static long totalTraveledDistance = 0;
    private static long totalCost = 0;
    private static long totalProfit = 0; //TODO: totalProfit-totalCost should be shown in statistics too
    private static long totalPassengersTransported = 0; //TODO: total profit on one passenger should be shown in statistics too


    /**
     * saves data about the course when it's done (linkProgress == 1)
     */
    public static void logArrival(Train train){
        StringBuilder builder = new StringBuilder();
        String lineSeperator = System.lineSeparator();

        builder.append("==========================");
        builder.append(lineSeperator);
        builder.append("Zakonczono trase pociagu: " + train.getName());
        builder.append(lineSeperator);
        builder.append("Stacja poczatkowa: " + train.getPreviousStation().name +"\nStacja koncowa: " + train.getNextStation().name);
        builder.append(lineSeperator);
        double distanceY = train.getNextStation().coordY - train.getPreviousStation().coordY;
        double distanceX = train.getNextStation().coordX - train.getPreviousStation().coordX;
        double distanceKM = Math.hypot(distanceX, distanceY)*0.875; //1km w rzeczywisto\u015bci = coords*0.875
        double cost  = distanceKM * train.getCostPerKM();
        builder.append("Dystans: " + distanceKM);
        builder.append(lineSeperator);
        builder.append("Koszt przejazdu: " + cost );
        builder.append(lineSeperator);
        builder.append("Ilosc pasazerow: " + train.getPassengers());
        builder.append(lineSeperator);
        double time = (System.currentTimeMillis() - train.getTesttime()) /1000; //for testing purposes
        builder.append("Czas: " + time ); //show how much time did it take to go between the stations
        builder.append(lineSeperator);
        double profit = train.getPassengers() * train.getProfitPerPassenger();
        builder.append("Zysk z bilet\u00f3w: " + profit);
        builder.append(lineSeperator);
        builder.append("Zysk po odj\u0119ciu koszt\u00f3w " + (profit - cost) );
        builder.append(lineSeperator);
        money -= cost;
        money += profit;
        totalTraveledDistance += distanceKM;
        totalCost+= cost;
        totalProfit += profit;
        totalPassengersTransported += train.getPassengers();

        String raport = builder.toString();
        System.out.println(raport);

        try {
            PrintWriter out = new PrintWriter(new FileWriter("rapport.txt", true));
            out.println(raport);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.err.println("failed writing the report");
            e.printStackTrace();
        }
    }

    /**
     * Sets the amount of money at the starts of the simulation
     *
     * @param initialMoneyAmount
     */
    public static void setMoney(int initialMoneyAmount){
        money = initialMoneyAmount;
    }
}
