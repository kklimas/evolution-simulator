package com.app.models;

import com.app.configurations.CustomConfiguration;

import java.util.Random;

import static com.app.configurations.DefaultConfiguration.*;

abstract class AbstractMapEntity implements IMapEntity{
    protected Vector2d position;
    protected CustomConfiguration configuration;
    private final Random random = new Random();

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector2d position) {
        this.position = position;
    }

    @Override
    public void selectPosition() {
        int xCoo = random.nextInt(configuration.mapWidth());
        int yCoo = random.nextInt(configuration.mapHeight());
        setPosition(new Vector2d(xCoo, yCoo));
    }

    @Override
    public int getTileSize() {
        var w = (WINDOW_WIDTH - INFO_HEIGHT) / configuration.mapWidth();
        var h = (WINDOW_HEIGHT - CHARTS_HEIGHT) / configuration.mapHeight();
        return Math.min(w, h);
    }
}
