package com.app.models;

import com.app.configurations.CustomConfiguration;

import java.util.Random;

abstract class AbstractMapEntity implements IMapEntity{
    protected Vector2d position;
    protected CustomConfiguration configuration;
    private Random random = new Random();

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
}
