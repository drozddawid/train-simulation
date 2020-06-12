package projekt;

public class StatisticsLogger {//TODO:(make it show in window and save to datafile) for now it just shows in console what statisticlogger should show on panel and save to some datafile
    private static int money = 0;
    private static long totalTraveledDistance = 0;
    private static long totalCost = 0;
    private static long totalProfit = 0; //TODO: totalProfit-totalCost should be shown in statistics too
    private static long totalPassengersTransported = 0; //TODO: total profit on one passenger should be shown in statistics too



    public static void logArrival(Train train){ //saves data about the course when it's done (linkProgress == 1)
        //TODO: this method should also save trip report (something like this below) to datafile
        System.out.println("==========================");
        System.out.println("Zakonczono trase pociagu: " + train.name);
        System.out.println("Stacja poczatkowa: " + train.previousStation.name +"\nStacja koncowa: " + train.nextStation.name);
        double distanceY = train.nextStation.coordY - train.previousStation.coordY;
        double distanceX = train.nextStation.coordX - train.previousStation.coordX;
        double distanceKM = Math.hypot(distanceX, distanceY)*0.875; //1km w rzeczywistości = coords*0.875
        double cost  = distanceKM * train.costPerKM;
        System.out.println("Dystans: " + distanceKM);
        System.out.println("Koszt przejazdu: " + cost );
        System.out.println("Ilosc pasazerow: " + train.passengers);
        double time = (System.currentTimeMillis() - train.testtime) /1000; //for testing purposes
        System.out.println("Czas: " + time ); //show how much time did it take to go between the stations
        double profit = train.passengers*train.profitPerPassenger;
        System.out.println("Zysk z biletów: " + profit);
        System.out.println("Zysk po odjęciu kosztów " + (profit - cost) );
        money -= cost;
        money += profit;
        totalTraveledDistance += distanceKM;
        totalCost+= cost;
        totalProfit += profit;
        totalPassengersTransported += train.passengers;

    }

    public static void setMoney(int initialMoneyAmount){
        money = initialMoneyAmount;
    }

    public static void reset(){
        money = Settings.getInitialMoneyAmount();
        totalTraveledDistance = 0;
        totalCost = 0;
        totalProfit = 0;
        totalPassengersTransported = 0;
    }
}
