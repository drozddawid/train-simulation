package projekt;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class that keeps all the routes and trains
 */
public class RouteManager {
    private HashMap<Integer, TrainRoute> routes;
    private HashMap<Integer, Train> trains = new HashMap<>();

    /**
     * Initializes routes from resources/timetable.json and corresponding trains from resources/trains.json
     *
     * @param stationDatabase the database with stations
     */
    RouteManager(StationDatabase stationDatabase){
        JSONObject routesFile = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/timetable.json")));
        routes = new HashMap<>();


        for(Object routesJsonObject : routesFile.getJSONArray("trainroutes")){
            JSONObject route = (JSONObject) routesJsonObject;

            int id = route.getInt("id");

            JSONArray stopsJsonArray = route.getJSONArray("stops");
            ArrayList<Integer> stopsByID = new ArrayList<>();

            if(stopsJsonArray != null) {
                for (int i = 0; i < stopsJsonArray.length(); i++) {
                    stopsByID.add((Integer) stopsJsonArray.get(i));
                }
            }
            routes.put(id, new TrainRoute(id, stopsByID, stationDatabase));
        }

        JSONObject trainsFile = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/trains.json")));
        for(Object trainsJsonObject : trainsFile.getJSONArray("trains")){
            JSONObject JSONtrain = (JSONObject) trainsJsonObject;

            int id = JSONtrain.getInt("trainID");

            Train train = new Train(
                    id,
                    JSONtrain.getString("name"),
                    JSONtrain.getInt("costPerKM"),
                    JSONtrain.getDouble("profitPerPassenger"),
                    JSONtrain.getInt("seats"),
                    JSONtrain.getInt("speed"),
                    this.getRouteByID(JSONtrain.getInt("currentTrainRouteID")));

            trains.put(id, train);
        }
    }

    /**
     *
     * @return all loaded Trains by their id
     */
    public HashMap<Integer, Train> getTrains (){ return this.trains;}

    /**
     *
     * @return a list of all loaded trains
     */

    public ArrayList<Train> getTrainsArrayList() { return new ArrayList<Train>(this.trains.values()); }

    /**
     *
     * @param id the train id
     * @return a loaded train by its id
     */
    public TrainRoute getRouteByID(int id){
        return routes.get(id);
    }

    /**
     *
     * @return all loaded routes
     */
    public HashMap<Integer, TrainRoute> getRoutes() {
        return routes;
    }
}
