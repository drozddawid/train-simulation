public class RouteTime {
    public static long secondsSinceMidnight = System.currentTimeMillis();

    public static long getTime(){ //zwraca czas od stworzenia obiektu RouteTime do wywołania RouteTime.getTime() w sekundach
        return (System.currentTimeMillis()-secondsSinceMidnight)*1000;
    }

}
