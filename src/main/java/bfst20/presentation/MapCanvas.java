package bfst20.presentation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.List;

import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;


import java.awt.*;

public class MapCanvas extends Canvas {

    Affine trans = new Affine();
    List<Way> data;
    List<Relation> islandRelations;

    public MapCanvas(Dimension dimension) {
        setWidth(dimension.getWidth());
        setHeight(dimension.getHeight());

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, dimension.getWidth(), dimension.getHeight());
    }

    public void update() {

    }

    public void initializeData() {
        Parser parser = Parser.getInstance();
        this.data = parser.getOSMWays();
        this.islandRelations = parser.getIslandRelations();
        repaint();
    }

    public void repaint() {
        GraphicsContext gc = getGraphicsContext2D();

        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(trans);
        
        double pixelwidth = 1 / Math.sqrt(Math.abs(trans.determinant()));
        gc.setLineWidth(pixelwidth);

        for (Drawable element : data) {
            element.Draw(gc);
        }
    }

    public void zoom(double factor, double x, double y) {
        trans.prependScale(factor, factor, x, y);
        repaint();
    }
}