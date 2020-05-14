import java.io.File;
import java.util.ArrayList;

public class MainWindow {
    public static void main (String[] args){
        RouteTime time = new RouteTime();

        File stationData = new File ("src/stationData.txt");
        File linkData = new File("src/linkData.txt");
        StationDatabase dataBase = new StationDatabase(stationData, linkData);
        System.out.println("===========STACJE=========");
        for(int i = 0; i<4;i++) {
            System.out.println(dataBase.stations.get(i).getStation());
        }
        System.out.println("===========POLACZENIA=========");
        for(int i = 0; i<5;i++) {
            System.out.println(dataBase.stationLinks.get(i).getLink());
        }
        System.out.println("Minelo " + RouteTime.getTime() + " ms");
    }
}
