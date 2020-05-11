import java.util.ArrayList;

public class Station extends MapObject {
    public String name;
    public double profitability;
    public ArrayList<Station> connectedWith;

    Station(String name, double profitability, ArrayList<Station> connectedWith){
        this.name = name;
        this.profitability = profitability;
        this.connectedWith = connectedWith;
    }
    public Station(){
        this("null",0, new ArrayList<>());
    }
    public String getStation(){
        return "\n" + this.name + " " + this.profitability + " " + this.connectedWith.toString();
    }
    @Override
    public void draw(){
        //draws Station
    }
}
