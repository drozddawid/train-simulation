package projekt;

import java.util.ArrayList;

public class Station extends MapObject {
    public int stationID;
    public String name;
    public double profitability;
    public ArrayList<Integer> connectedWith;

    Station(int stationID, String name, double profitability,double coordX,double coordY, ArrayList<Integer> connectedWith){
        this.stationID = stationID;
        this.name = name;
        this.profitability = profitability;
        this.coordX = coordX;
        this.coordY = coordY;
        this.connectedWith = connectedWith;
    }
    public Station(){
        this(0,"null", 0, 0, 0, new ArrayList<Integer>());
    }
    public String getStation(){
        return "\nprojekt.Station ID: " + this.stationID +  "\nname: " + this.name + "\nProfitability: "
                + this.profitability + "\ncoordX: " + this.coordX + "\ncoordY: " + this.coordY
                + "\nConnected with (ID's): " + connectedWith.toString();
    }
    @Override
    public void draw(){
        //draws projekt.Station
    }
}
