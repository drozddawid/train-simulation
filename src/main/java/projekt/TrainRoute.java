package projekt;


import java.util.ArrayList;

/**
 * A route for a train.
 *
 * A route consists of multiple Stations
 */
public class TrainRoute {
    public int routeID;
    public int stopsByIDSize;
    private ArrayList<Station> stops;

    /**
     * Constructs a TrainRoute
     *
     * @param routeID between [1, INT_MAX]
     * @param stopsByID
     * @param stationDatabase
     */
    TrainRoute(int routeID, ArrayList<Integer> stopsByID, StationDatabase stationDatabase){
        stops = new ArrayList<>();
        this.routeID = routeID;
        stopsByIDSize = stopsByID.size();

        for (Integer integer : stopsByID) {
            stops.add(stationDatabase.getStationsById().get(integer));
        }
    }


    public ArrayList<Station> getStops(){ return this.stops;}

    public Station getStop(int stopID){
        return this.stops.get(stopID);
    }
}
