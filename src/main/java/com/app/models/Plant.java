package com.app.models;

import com.app.configurations.CustomConfiguration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static com.app.configurations.DefaultConfiguration.INFO_HEIGHT;
import static com.app.configurations.DefaultConfiguration.WINDOW_HEIGHT;

public class Plant extends AbstractMapEntity {

    private final int tileSize;

    public Plant(CustomConfiguration configuration) {
        this.configuration = configuration;
        this.tileSize = (WINDOW_HEIGHT - INFO_HEIGHT) / configuration.mapHeight();
        selectPosition();
    }

    @Override
    public Shape getShape() {
        var rec = new Rectangle(tileSize, tileSize);
        rec.setFill(Color.GREEN);
        return rec;
    }
}
