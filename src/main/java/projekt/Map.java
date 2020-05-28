package projekt;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class Map {
    private ArrayList<MapObject> objects;
    private StationDatabase stationDatabase;
    private RouteManager routeManager;
    private RouteTime routeTime;
    private StatisticsLogger statisticsLogger;
    private Canvas canvas = new Canvas(900, 800);
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private String loadedCountrySvgPath;

    public Map() {
        advanceTime();
        getCountrySvgPath();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    /* Runs about 30 times a second */
    public void advanceTime() {
        clearMap();
    }

    private void clearMap() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Paint.valueOf("#ecedd3"));
        gc.setStroke(Paint.valueOf("#aeb071"));
        gc.appendSVGPath(getCountrySvgPath());
        gc.fill();
        gc.stroke();
    }

    private String getCountrySvgPath() {
        if (loadedCountrySvgPath != null)
            return loadedCountrySvgPath;

        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/map.xml")) {
            properties.loadFromXML(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadedCountrySvgPath = (String) properties.get("map.poland.svgPath");

        return loadedCountrySvgPath;
    }
}
