package projekt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class StationLink extends MapObject{
    public Station from;
    public Station to;

    StationLink(Station from, Station to){
        this.from = from;
        this.to = to;
    }

    @Override
    public void tick() {

    }

    @Override
    public void draw(GraphicsContext gc){
        gc.setStroke(Paint.valueOf("#a0a0a0"));
        gc.setLineWidth(1);
        gc.strokeLine(from.coordX, from.coordY, to.coordX, to.coordY);
    }
}
