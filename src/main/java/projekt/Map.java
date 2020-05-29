package projekt;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Map {
    private ArrayList<MapObject> objects = new ArrayList<>();
    private StationDatabase stationDatabase;
    private RouteManager routeManager;
    private RouteTime routeTime;
    private StatisticsLogger statisticsLogger;
    private Canvas canvas = new Canvas(900, 800);
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private String loadedCountrySvgPath;

    public Map(StationDatabase sDB) {
        stationDatabase = sDB;

        // Add stationLinks first so they get drawn first
        objects.addAll(stationDatabase.getStationLinks());
        objects.addAll(stationDatabase.getStations().values());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    /* Runs about 30 times a second */
    public void advanceTime() {
        gc.save();
        clearMap();
        gc.restore();

        for(MapObject obj : objects) {
            gc.save();
            obj.draw(gc);
            gc.restore();
        }
    }

    private void clearMap() {
        // Clear everything
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Paint Poland with an outline
        gc.setFill(Paint.valueOf("#ecedd3"));
        gc.setStroke(Paint.valueOf("#aeb071"));
        gc.beginPath();
        gc.appendSVGPath(ObjectPathResourceGetter.getInstance().getValue("svgPath.map.poland"));
        gc.closePath();
        gc.fill();
        gc.stroke();
    }

}
