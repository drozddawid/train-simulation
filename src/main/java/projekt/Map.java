package projekt;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Map {
    private ArrayList<MapObject> objects;
    private StationDatabase stationDatabase;
    private RouteManager routeManager;
    private RouteTime routeTime;
    private StatisticsLogger statisticsLogger;
    private Canvas canvas;
    private GraphicsContext gc;
    private String loadedCountrySvgPath;
    private double mouseX = 0;
    private double mouseY = 0;

    public Map(StationDatabase sDB) {
        stationDatabase = sDB;
        objects = new ArrayList<>();
        statisticsLogger = new StatisticsLogger();
        canvas = new Canvas(900,800);
        gc = canvas.getGraphicsContext2D();

   /*     // Add stationLinks first so they get drawn first
        objects.addAll(stationDatabase.getStationLinks()); I MOVED THIS TO AddObjects() (below) in order to allow button "startSimulating" (in class MainWindow, RouteManagerScene method) put objects on map after changing settings
        objects.addAll(stationDatabase.getStationsById().values());
   */

        getCanvas().setOnMouseMoved(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
        });
    }

    public void addObjects(StationDatabase stationDatabase, RouteManager routeManager){
        // Add stationLinks first so they get drawn first
        objects.addAll(stationDatabase.getStationLinks());
        objects.addAll(stationDatabase.getStationsById().values());
        objects.addAll(routeManager.getTrains().values());
    }

    public void clearObjects(){
        objects.clear();
    }
    public Canvas getCanvas() {
        return canvas;
    }

    /* Runs about 30 times a second */
    public void advanceTime() {
        gc.save();
        gc.beginPath();
        clearMap();
        gc.restore();

        // NOTE: if this loop is ever gonna be a significant overhead, move to a seperate thread
        for(MapObject obj : objects) {
            obj.tick();
        }

        for(MapObject obj : objects) {
            gc.save();
            gc.beginPath();
            obj.draw(gc);
            gc.restore();
        }

        drawInfoBox();
    }

    private void drawInfoBox() {
        double closestDistanceMouseToTrain = 1000;
        Train closestTrain = null;

        for(MapObject obj : objects) {
            if(obj instanceof Train) {
                double distanceToMouse = Math.abs(obj.coordX - mouseX) + Math.abs(obj.coordY - mouseY);
                if(distanceToMouse < closestDistanceMouseToTrain) {
                    closestDistanceMouseToTrain = distanceToMouse;
                    closestTrain = (Train) obj;
                }
            }
        }

        // No trains on the map
        if(closestTrain == null)
            return;

        // Don't display the tooltip if the mouse is too far away
        if(closestDistanceMouseToTrain > 25)
            return;

        gc.save();
        gc.beginPath();
        closestTrain.drawInfoBox(gc);
        gc.restore();
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
