package com.app.models;

import com.app.Simulation;
import com.app.configurations.CustomConfiguration;
import com.app.dtos.DrawDTO;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.app.configurations.DefaultConfiguration.DELAY;

public class Engine implements Runnable{

    private final Simulation simulation;
    private final CustomConfiguration configuration;
    private final WorldMap worldMap;
    private final ExecutorService executorService;
    private final List<Animal> animals = new ArrayList<>();
    private final List<Plant> plants = new ArrayList<>();

    private final Stage stage;
    private final Button runButton;
    private int deadCount = 0;
    private int currentDay = 1;

    private boolean running = false;
    private boolean exit = false;

    public Engine(Simulation simulation, CustomConfiguration configuration, WorldMap worldMap, Stage stage, Button button) {
        this.simulation = simulation;
        this.configuration = configuration;
        this.worldMap = worldMap;
        this.stage = stage;
        this.runButton = button;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void run() {
        handleButtonClick();
        placeEntities();
        draw();
    }
    private void execute() {
        while (running && !exit) {
            draw();
            die();
            move();
            eat();
            reproduce();
            grow();
            currentDay++;
        }
    }
    private void handleButtonClick() {
        runButton.setOnMouseClicked(event -> {
            running = !running;
            if (running) {
                executorService.submit(this::execute);
            }
        });
        runButton.setStyle("-fx-cursor: hand;");

        stage.setOnCloseRequest(event -> exit = true);
    }
    private void placeEntities() {
        // animals
        for (int i = 0; i < configuration.animalsStartNumber(); i++) {
            var animal = new Animal(configuration, worldMap);
            worldMap.place(animal);
            animals.add(animal);
        }
        // plants
        for (int i = 0; i < configuration.startPlantsNumber(); i++) {
            var plant = new Plant(configuration);
            if (worldMap.place(plant)) {
                plants.add(plant);
            }
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
        deadCount += deadMark.size();
    }
    private void move() {
        animals.forEach(Animal::move);
    }
    private void eat() {
        List<Plant> eatenPlants = new ArrayList<>();
        plants.forEach(plant -> {
            var strongest = worldMap.objectsAt(plant.getPosition()).first();
            if (strongest.getClass().equals(Animal.class)) {
                var animal = (Animal) strongest;
                animal.eat();
                animals.remove(animal);
                animals.add(animal);
                eatenPlants.add(plant);
            }
        });
        plants.removeAll(eatenPlants);
        worldMap.removeAll(eatenPlants.stream().map(IMapEntity.class::cast).toList());
    }
    private void reproduce() {

    }
    private void grow() {
        for (int i = 0; i < configuration.newPlantsEveryDay(); i++) {
            var plant = new Plant(configuration);
            if (worldMap.place(plant)){
                plants.add(plant);
            }
        }
    }

    private void draw() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ignored) {}
        this.simulation.draw(prepareDTO());
        if (animals.isEmpty()) {
            exit = true;
        }
    }

    private DrawDTO prepareDTO() {
        List<IMapEntity> entitiesToDraw = new ArrayList<>(animals.stream().map(IMapEntity.class::cast).toList());
        var plantsAsEntities = plants.stream().map(IMapEntity.class::cast).toList();
        entitiesToDraw.addAll(plantsAsEntities);

        var averageEnergy = !animals.isEmpty() ? animals.stream()
                .map(Animal::getCurrentEnergy).mapToInt(Integer::intValue).sum() / animals.size()
                : 0;

        var freePlaces = configuration.mapWidth() * configuration.mapHeight() - animals.size() - plants.size();

        return new DrawDTO(
                currentDay,
                entitiesToDraw,
                animals.size(),
                averageEnergy,
                plants.size(),
                freePlaces,
                deadCount,
                "[1, 2, 1, 0, 2, 1, 0, 5, 2]"

        );
    }
}
