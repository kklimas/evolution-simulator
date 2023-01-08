package com.app.entities;

import com.app.configurations.CustomConfiguration;
import com.app.enums.variants.PlantGrowVariant;
import com.app.models.Vector2d;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.HashSet;
import java.util.Random;

import static com.app.configurations.DefaultConfiguration.EQUATOR_PERCENTAGE_HEIGHT;

public class Plant extends AbstractMapEntity {

    private final Random random = new Random();
    private final HashSet<Vector2d> deadPlaces;

    public Plant(CustomConfiguration configuration) {
        this.configuration = configuration;
        this.deadPlaces = new HashSet<>();
        selectPosition();
    }

    public Plant(CustomConfiguration configuration, HashSet<Vector2d> deadVectors) {
        this.configuration = configuration;
        this.deadPlaces = deadVectors;
        selectPosition();
    }

    @Override
    public Shape getShape() {
        var tileSize = getTileSize();
        var rec = new Rectangle(tileSize, tileSize);
        rec.setFill(Color.GREEN);
        return rec;
    }

    @Override
    public void selectPosition() {
        var choice = random.nextInt(5);

        if (PlantGrowVariant.FORESTED_AREA == configuration.plantGrowVariant()) {
            int xCoo = random.nextInt(configuration.mapWidth());
            int yCoo;
            var middle = configuration.mapHeight() / 2;
            var r = EQUATOR_PERCENTAGE_HEIGHT * configuration.mapHeight() / 2;

            if (choice < 4) {
                var max = (int) Math.ceil(middle + r);
                var min = (int) Math.ceil(middle - r);
                yCoo = random.nextInt(max - min) + min;
            } else {
                var side = random.nextInt(2);
                int min;
                int max;
                if (side == 0) {
                    min = 0;
                    max = (int) Math.ceil(middle - r);
                } else {
                    min = (int) Math.ceil(middle + r);
                    max = configuration.mapHeight();
                }
                yCoo = random.nextInt(max - min) + min;
            }
            setPosition(new Vector2d(xCoo, yCoo));
        } else {
            if (choice == 4 && !deadPlaces.isEmpty()) {
                var vectorIndex = random.nextInt(deadPlaces.size());
                var vector = deadPlaces.stream().toList().get(vectorIndex);
                setPosition(vector);
            }
            else {
                var xCoo = random.nextInt(configuration.mapWidth());
                var yCoo = random.nextInt(configuration.mapHeight());
                var vector = new Vector2d(xCoo, yCoo);
                if (!deadPlaces.contains(vector)) {
                    setPosition(vector);
                }
            }
        }
    }
}
