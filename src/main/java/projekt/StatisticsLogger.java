package projekt;

public class StatisticsLogger {// for now it just shows in console what statisticlogger should show on panel and save to some datafile
    public static void logArrival(Train train){ //saves data about the course when it's done (linkProgress == 1)
        System.out.println("==========================");
        System.out.println("Zakonczono trase pociagu: " + train.name);
        System.out.println("Stacja poczatkowa: " + train.previousStation.name +"\nStacja koncowa: " + train.nextStation.name);
        double distanceY = train.nextStation.coordY - train.previousStation.coordY;
        double distanceX = train.nextStation.coordX - train.previousStation.coordX;
        double distance = Math.hypot(distanceX, distanceY)*0.875; //1km w rzeczywistości = coords*0.875
        double cost  = distance * train.costPerKM;
        System.out.println("Dystans: " + distance);
        System.out.println("Koszt przejazdu: " + cost );
        System.out.println("Ilosc pasazerow: " + train.passengers);
        double time = (System.currentTimeMillis() - train.testtime) /1000;
        System.out.println("Czas: " + time );
        double profit = train.passengers*train.profitPerPassenger;
        System.out.println("Zysk z biletów: " + profit);
        System.out.println("Zysk po odjęciu kosztów " + (profit - cost) );
    }
}
