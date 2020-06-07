package projekt;

//import com.sun.javafx.sg.prism.NGCanvas; //TODO: fix this line - it gives some err
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Station extends MapObject {
    public int stationID;
    public String name;
    public double profitability;
    public ArrayList<Station> connectedWith;

    Station(int stationID, String name, double profitability,double coordX,double coordY) {
        this.stationID = stationID;
        this.name = name;
        this.profitability = profitability;
        this.coordX = coordX;
        this.coordY = coordY;
        this.connectedWith = new ArrayList<>();
    }

    public void connectTo(Station st) {
        connectedWith.add(st);
    }

    public Station(){
        this(0,"null", 0, 0, 0);
    }

    public String getStation(){
        return "\nStation ID: " + this.stationID +  "\nname: " + this.name + "\nProfitability: "
                + this.profitability + "\ncoordX: " + this.coordX + "\ncoordY: " + this.coordY
                + "\nConnected with (ID's): " + connectedWith.toString();
    }

    @Override
    public void tick() {

    }

    @Override
    public void draw(GraphicsContext gc){
        double radius = 7.0;
        gc.fillOval(coordX - radius/2.0, coordY - radius/2.0, radius, radius);

        gc.setFill(Paint.valueOf("#505050"));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText(name, coordX, coordY + radius/2.0);
    }
}
