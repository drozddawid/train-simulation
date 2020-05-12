import java.util.ArrayList;

public class MainWindow {
    public static void main (String[] args){
        RouteTime time = new RouteTime();
        ArrayList<Station> emptyList = new ArrayList<>(0);

        Station station1 = new Station ("station1",2.3, emptyList);
        Station station2 = new Station ("station2",2, emptyList);

        StationLink link = new StationLink (station1,station2);
//testtestetest
        System.out.println(link.getLink());
        int i = 0;
        while(i<1000) {
            System.out.println(10 / 3 + " ");
            i++;
        }
        System.out.println("Minelo " + RouteTime.getTime() + " ms");
    }
}
