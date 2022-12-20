package com.app.models;

public class Plant extends AbstractMapEntity {
    public Plant(int mapWidth, int mapHeight) {
        selectPosition(mapWidth, mapHeight);
    }

    private void selectPosition(int width, int height) {
        int xCoo = (int) Math.floor(Math.random() * (width + 1));
        int yCoo = (int) Math.floor(Math.random() * (height + 1));
        setPosition(new Vector2d(xCoo, yCoo));
    }
}
