package projekt;

public class StatisticsLogger {// for now it just shows in console what statisticlogger should show on panel and save to some datafile
    private static int money;
    private long totalTraveledDistance;
    private long totalCost;
    private long totalProfit; //TODO: totalProfit-totalCost should be shown in statistics too
    private long totalPassengersTransported; //TODO: total profit on one passenger should be shown in statistics too

    StatisticsLogger(){
        this.money = 0;
        this.totalTraveledDistance = 0;
        this.totalCost = 0;
        this.totalProfit = 0;
        this.totalPassengersTransported = 0;
    }

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
        double time = (System.currentTimeMillis() - train.testtime) /1000;
        System.out.println("Czas: " + time );
        double profit = train.passengers*train.profitPerPassenger;
        System.out.println("Zysk z biletów: " + profit);
        System.out.println("Zysk po odjęciu kosztów " + (profit - cost) );
        money -= cost;
        money += profit;

    }

    public static void setMoney(int initialMoneyAmount){
        money = initialMoneyAmount;
    }
}
