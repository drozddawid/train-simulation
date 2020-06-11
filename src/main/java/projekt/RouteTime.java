package projekt;

public class RouteTime {
    public static long secondsSinceMidnight = System.currentTimeMillis();

    public static long getTime(){ //zwraca czas od stworzenia obiektu projekt.RouteTime do wywo\u0142ania projekt.RouteTime.getTime() w sekundach
        return (System.currentTimeMillis()-secondsSinceMidnight)*1000;
    }

}
