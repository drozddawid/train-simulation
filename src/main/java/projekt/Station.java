package projekt;

//import com.sun.javafx.sg.prism.NGCanvas; //TODO: fix this line - it gives some err
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Station extends MapObject {
    /**
     * The ID of a Station
     */
    public int stationID;
    /**
     * A name of a station (for example, "Warszawa")
     */
    public String name;

    /**
     * How profitable a station is
     */
    public double profitability;

    /**
     * A list of Stations connected to this Station with StaitonLinks
     */
    public ArrayList<Station> connectedWith;

    /**
     * Constructs a Station.
     *
     * @param stationID the ID of a station, in range [1, INT_MAX]
     * @param name the name of a station
     * @param profitability how much a station is profitable
     * @param coordX location: the X coordinate
     * @param coordY location: the Y coordinate
     */
    Station(int stationID, String name, double profitability,double coordX,double coordY) {
        this.stationID = stationID;
        this.name = name;
        this.profitability = profitability;
        this.coordX = coordX;
        this.coordY = coordY;
        this.connectedWith = new ArrayList<>();
    }

    /**
     * Mark the station as connected to another one
     * @param st the station to connect to
     */
    public void connectTo(Station st) {
        connectedWith.add(st);
    }

    /**
     * A station doesn't need to compute anything between frames
     */
    @Override
    public void tick() {

    }

    /**
     * Draw a Station
     *
     * @param gc the context on which to draw the object
     */
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
