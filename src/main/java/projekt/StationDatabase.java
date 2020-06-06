package projekt;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;


public class StationDatabase {
    private HashMap<Integer, Station> stationsById = new HashMap<>();
    private HashMap<String, Station> stationsByName = new HashMap<>();
    private ArrayList<StationLink> stationLinks = new ArrayList<>();

    StationDatabase(){ //makes stations and connections database based on data files (stations.json)
        JSONObject stationsFile = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/stations.json")));

        for(Object stationJsonObject : stationsFile.getJSONArray("stations")) {
            JSONObject s = (JSONObject) stationJsonObject;
            int id = s.getInt("id");
            String name = s.getString("name");
            Station station = new Station(
                    id,
                    name,
                    s.getDouble("profitability"),
                    s.getDouble("coordX"),
                    s.getDouble("coordY"));
            stationsById.put(id, station);
            stationsByName.put(name, station);
        }

        for(Object linkJsonObject : stationsFile.getJSONArray("links")) {
            JSONArray link = (JSONArray) linkJsonObject;
            for(int i = 0; i < link.length() - 1; i++) {
                linkTwoStations(link.getInt(i), link.getInt(i + 1));
            }

        }

    }

    private void linkTwoStations(int id1, int id2) {
        Station s1 = getStationsById().get(id1);
        Station s2 = getStationsById().get(id2);
        s1.connectTo(s2);
        s2.connectTo(s1);

        stationLinks.add(new StationLink(s1, s2));
    }

    public ArrayList<StationLink> getStationLinks() {
        return stationLinks;
    }

    public HashMap<Integer, Station> getStationsById() {
        return stationsById;
    }

    public HashMap<String, Station> getStationsByName() {
        return stationsByName;
    }

    public Station findStation(String stationName) {
        return stationsByName.get(stationName);
    }

    public Station findStation(int stationId) {
        return stationsById.get(stationId);
    }
}

