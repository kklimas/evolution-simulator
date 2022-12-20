package com.app.models;

import com.app.configurations.CustomConfiguration;
import com.app.enums.MoveDirection;

import java.util.List;

public class Animal extends AbstractMapEntity {
    private final CustomConfiguration configuration;
    private int currentEnergy;
    private List<MoveDirection> genotype;
    private int currentGenomeIndex;

    public Animal(CustomConfiguration configuration) {
        this.configuration = configuration;
        selectPosition(configuration);
    }

    private void selectPosition(CustomConfiguration configuration) {
        int xCoo = (int) Math.floor(Math.random() * (configuration.mapWidth() + 1));
        int yCoo = (int) Math.floor(Math.random() * (configuration.mapHeight() + 1));
        setPosition(new Vector2d(xCoo, yCoo));
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }
}
