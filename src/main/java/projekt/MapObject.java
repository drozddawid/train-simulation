package projekt;

import javafx.scene.canvas.GraphicsContext;

public abstract class MapObject {
    public double coordX;
    public double coordY;

    public abstract void tick();
    public abstract void draw(GraphicsContext gc);
}
