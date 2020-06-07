package projekt;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteManager {
    public HashMap<Integer, TrainRoute> routes;

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
    }
   /* public projekt.TrainRoute getTrainRoute(String trainName){
        //gets train route
    }*/
}
