package projekt;

import javafx.scene.canvas.GraphicsContext;

/**
 * An abstract object drawn on the Map
 */
public abstract class MapObject {
    /**
     * The X coordinate of an object
     */
    public double coordX;

    /**
     * The Y coordinate of an object
     */
    public double coordY;

    /**
     * Does computations before drawing on the map, such as moving a train forward
     */
    public abstract void tick();

    /**
     * Draws the MapObject on a Map
     *
     * @param gc the context on which to draw the object
     */
    public abstract void draw(GraphicsContext gc);
}
