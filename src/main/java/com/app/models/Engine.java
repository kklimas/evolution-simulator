package com.app.models;

import com.app.Simulation;
import com.app.configurations.CustomConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Engine implements Runnable{

    private Simulation simulation;
    private final CustomConfiguration configuration;
    private final IWorldMap worldMap;
    private final List<Animal> animals = new ArrayList<>();
    private final List<Plant> plants = new ArrayList<>();

    private boolean running = true;

    public Engine(Simulation simulation, CustomConfiguration configuration, IWorldMap worldMap) {
        this.simulation = simulation;
        this.configuration = configuration;
        this.worldMap = worldMap;
        placeEntities();
    }

    @Override
    public void run() {
        while (running) {
            die();
            move();
            eat();
            reproduce();
            grow();
        }
    }

    private void placeEntities() {
        // animals
        for (int i = 0; i < 1; i++) {
            var animal = new Animal(configuration, worldMap);
            worldMap.place(animal);
            animals.add(animal);
        }
        // plants
        for (int i = 0; i < configuration.startPlantsNumber(); i++) {
            var plant = new Plant(configuration);
            worldMap.place(plant);
            plants.add(plant);
        }
    }
    private void die() {
        List<Animal> deadMark = new ArrayList<>();
        animals.forEach(animal -> {
            if (animal.getCurrentEnergy() == 0) {
                deadMark.add(animal);
            }
        });
        animals.removeAll(deadMark);
        worldMap.removeAll(deadMark.stream().map(IMapEntity.class::cast).toList());

    }
    private void move() {
        animals.forEach(Animal::move);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.simulation.draw();
    }
    private void eat() {

    }
    private void reproduce() {

    }
    private void grow() {
        for (int i = 0; i < configuration.newPlantsEveryDay(); i++) {
            worldMap.place(new Plant(configuration));
        }
    }
}
