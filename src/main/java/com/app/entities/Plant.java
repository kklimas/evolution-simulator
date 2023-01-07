package com.app.entities;

import com.app.configurations.CustomConfiguration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Plant extends AbstractMapEntity {

    public Plant(CustomConfiguration configuration) {
        this.configuration = configuration;
        selectPosition();
    }

    @Override
    public Shape getShape() {
        var tileSize = getTileSize();
        var rec = new Rectangle(tileSize, tileSize);
        rec.setFill(Color.GREEN);
        return rec;
    }
}
