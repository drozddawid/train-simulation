import java.util.ArrayList;

public class Station extends MapObject {
    public int stationID;
    public String name;
    public double profitability;
    public ArrayList<Station> connectedWith;

    Station(int stationID, String name, double profitability, ArrayList<Station> connectedWith){
        this.stationID = stationID;
        this.name = name;
        this.profitability = profitability;
        this.connectedWith = connectedWith;
    }
    public Station(){
        this(0,"null",0, new ArrayList<>());
    }
    public String getStation(){
        return "\n" + this.name + " " + this.profitability + " " + this.connectedWith.toString();
    }
    @Override
    public void draw(){
        //draws Station
    }
}
