package com.app.utils;

import com.app.Simulation;
import com.app.configurations.CustomConfiguration;
import com.app.dtos.DrawDTO;
import com.app.entities.Animal;
import com.app.entities.IMapEntity;
import com.app.entities.Plant;
import com.app.enums.MoveDirection;
import com.app.enums.variants.MapVariant;
import com.app.models.Vector2d;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.app.configurations.DefaultConfiguration.DELAY;

public class Engine implements Runnable {

    private final Simulation simulation;
    private final CustomConfiguration configuration;
    private final ExecutorService executorService;
    private final Map<Vector2d, SortedSet<IMapEntity>> entities;
    private final HashSet<Vector2d> deadPlaces = new HashSet<>();
    private final Random random = new Random();
    private final Stage stage;
    private final Button runButton;
    private int deadCount = 0;
    private int currentDay = 1;
    private int totalLifespanOfDeadAnimals = 0;
    private boolean running = false;
    private boolean exit = false;
    private Animal selectedAnimal;

    public Engine(Simulation simulation, CustomConfiguration configuration, Stage stage, Button button) {
        this.simulation = simulation;
        this.configuration = configuration;
        this.stage = stage;
        this.runButton = button;
        this.executorService = Executors.newFixedThreadPool(5);
        entities = new HashMap<>();
    }

    @Override
    public void run() {
        handleButtonClick();
        placeEntities();
        draw(false);
        saveData();
    }

    private void execute() {
        while (running && !exit) {
            currentDay++;
            die();
            move();
            eat();
            reproduce();
            grow();
            saveData();
            draw(false);
        }
    }

    private void handleButtonClick() {
        runButton.setOnMouseClicked(event -> {
            running = !running;
            if (running) {
                executorService.submit(this::execute);
            } else {
                draw(true);
            }
        });
        runButton.setStyle("-fx-cursor: hand;");

        stage.setOnCloseRequest(event -> {
            exit = true;
            executorService.shutdown();
        });
    }

    private void placeEntities() {
        // animals
        for (int i = 0; i < configuration.animalsStartNumber(); i++) {
            var animal = new Animal(configuration, this);
            place(animal);
        }
        // plants
        for (int i = 0; i < configuration.startPlantsNumber(); i++) {
            var plant = new Plant(configuration);
            place(plant);
        }
    }

    private void die() {
        List<Animal> deadMark = new ArrayList<>();
        var animals = listEntities(true).stream().map(Animal.class::cast).toList();
        animals.forEach(animal -> {
            if (animal.getCurrentEnergy() == 0) {
                deadMark.add(animal);
            }
        });
        if (!deadMark.isEmpty()) {
            removeAll(deadMark.stream().map(IMapEntity.class::cast).toList());
            deadCount += deadMark.size();
            deadMark.forEach(animal -> {
                animal.setDayOfDeath(currentDay);
                deadPlaces.add(animal.getPosition());
                totalLifespanOfDeadAnimals += animal.getDaysLiving();
            });
        }
    }

    private void move() {
        var animals = listEntities(true).stream()
                .map(Animal.class::cast)
                .toList();
        animals.forEach(animal -> {
            move(animal);
            animal.move();
        });
    }

    private void eat() {
        List<Plant> eatenPlants = new ArrayList<>();
        var plants = listEntities(false)
                .stream()
                .map(Plant.class::cast)
                .toList();

        plants.forEach(plant -> {
            var strongest = objectsAt(plant.getPosition()).first();
            if (strongest.getClass().equals(Animal.class)) {
                var animal = (Animal) strongest;
                animal.eat();
                eatenPlants.add(plant);
            }
        });
        removeAll(eatenPlants.stream().map(IMapEntity.class::cast).toList());
    }

    private void reproduce() {
        // two strongest animals of each position create a child
        List<List<Animal>> families = new ArrayList<>();
        var takenPlaces = entities.keySet();
        takenPlaces.forEach(v -> {
            var setOfEntities = objectsAt(v);
            Animal animal1 = null;
            Animal animal2 = null;
            if (setOfEntities.size() > 1) {
                var iterator = setOfEntities.iterator();
                var count = 0;
                while (iterator.hasNext() && count < 2) {
                    var entity = iterator.next();
                    if (!entity.getClass().equals(Animal.class)) break;
                    if (count == 0) {
                        animal1 = (Animal) entity;
                    } else {
                        animal2 = (Animal) entity;
                    }
                    count++;
                }
            }
            if (isReadyForReproduction(animal1) && isReadyForReproduction(animal2)) {
                var child = animal1.getChild(animal2);

                animal1.reproduce();
                animal2.reproduce();

                families.add(List.of(animal1, animal2, child));
            }
        });
        families.forEach(family -> {
            var a1 = family.get(0);
            var a2 = family.get(1);
            var ch = family.get(2);
            reproduce(a1, a2, ch);
        });
    }

    private void grow() {
        for (int i = 0; i < configuration.newPlantsEveryDay(); i++) {
            var plant = new Plant(configuration);
            place(plant);
        }
    }

    private void draw(boolean showDominant) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ignored) {}
        var dataDTO = prepareDTO();
        this.simulation.draw(dataDTO, showDominant);
        if (listEntities(true).isEmpty()) {
            exit = true;
        }
    }

    private DrawDTO prepareDTO() {
        var plantsEntities = listEntities(false);
        var animalsEntities = listEntities(true);
        List<IMapEntity> entitiesToDraw = new ArrayList<>(plantsEntities);
        entitiesToDraw.addAll(animalsEntities);

        var averageAnimalLifespan = deadCount != 0 ?
                totalLifespanOfDeadAnimals / deadCount : 0;

        var animals = animalsEntities.stream().map(Animal.class::cast).toList();

        var averageEnergy = !animalsEntities.isEmpty() ? animals.stream()
                .map(Animal::getCurrentEnergy).mapToInt(Integer::intValue).sum() / animalsEntities.size()
                : 0;

        var freePlaces = configuration.mapWidth() * configuration.mapHeight() - entities.keySet().size();

        var dominantGenotype = getDominantGenotype().toString();

        return new DrawDTO(
                currentDay,
                entitiesToDraw,
                animalsEntities.size(),
                averageEnergy,
                averageAnimalLifespan,
                plantsEntities.size(),
                freePlaces,
                deadCount,
                dominantGenotype,
                selectedAnimal
        );
    }

    private List<Integer> getDominantGenotype() {
        Map<List<Integer>, Integer> genotypes = new HashMap<>();
        int currentBest = 0;
        List<Integer> dominantGenotype = new ArrayList<>();
        var animals = listEntities(true)
                .stream()
                .map(Animal.class::cast)
                .toList();

        for (var animal : animals) {
            var g = animal.getGenotype();
            if (genotypes.containsKey(g)) {
                var previous = genotypes.get(g);
                genotypes.put(g, previous + 1);
                if (previous + 1 > currentBest) {
                    currentBest = previous + 1;
                    dominantGenotype = g;
                }
            } else {
                genotypes.put(g, 1);
                if (1 > currentBest) {
                    currentBest = 1;
                    dominantGenotype = g;
                }
            }
        }
        return dominantGenotype;
    }

    private boolean isReadyForReproduction(Animal animal) {
        return animal != null
                && animal.getCurrentEnergy() >= configuration.energyNeedToReproduce()
                && animal.isReadyForReproduction();
    }

    private void saveData() {
        var dto = prepareDTO();
        this.simulation.updateCSV(dto);
    }

    private void place(IMapEntity entity) {
        var placeTaken = entities.containsKey(entity.getPosition());
        if (placeTaken) {
            if (entity.getClass().equals(Plant.class)
                    && entities.get(entity.getPosition()).last().getClass().equals(Plant.class)) {
                return;
            }
            entities.get(entity.getPosition()).add(entity);
        } else {
            var treeSet = new TreeSet<>(new EntitiesComparator());
            treeSet.add(entity);
            entities.put(entity.getPosition(), treeSet);
        }
    }

    private List<IMapEntity> listEntities(boolean isAnimal) {
        var a = entities.keySet().stream()
                .map(vector -> {
                    var entitiesAtVector = entities.get(vector);
                    return entitiesAtVector.stream()
                            .filter(e -> isAnimal
                                    ? e.getClass().equals(Animal.class)
                                    : e.getClass().equals(Plant.class))
                            .toList();
                })
                .toList();

        var iMapEntities = new ArrayList<IMapEntity>();
        a.forEach(iMapEntities::addAll);

        return iMapEntities;
    }

    private void removeAll(List<IMapEntity> entitiesList) {
        entitiesList.forEach(this::remove);
    }

    private void remove(IMapEntity entity) {
        var set = entities.get(entity.getPosition());
        if (set != null) {
            set.remove(entity);
            if (set.isEmpty()) {
                entities.remove(entity.getPosition());
            }
        }
    }

    private SortedSet<IMapEntity> objectsAt(Vector2d position) {
        return entities.get(position);
    }

    private void reproduce(Animal animal1, Animal animal2, Animal child) {
        stateChanged(animal1);
        stateChanged(animal2);
        place(child);
    }

    private void stateChanged(Animal animal) {
        remove(animal);
        place(animal);
    }

    private void move(Animal animal) {

        remove(animal);

        // move animal depends on map variant
        var nextPosition = animal.getPosition().add(animal.getDirection().toVector2d());
        if (configuration.mapVariant().equals(MapVariant.GLOBE)) {
            if (nextPosition.getX() >= configuration.mapWidth()) {
                nextPosition.setX(0);
            } else if (nextPosition.getX() < 0) {
                nextPosition.setX(configuration.mapWidth() - 1);
            }
            if (nextPosition.getY() < 0) {
                nextPosition.setY(0);
                animal.setDirection(MoveDirection.S);
            } else if (nextPosition.getY() >= configuration.mapHeight()) {
                nextPosition.setY(configuration.mapHeight() - 1);
                animal.setDirection(MoveDirection.N);
            }
            animal.setPosition(nextPosition);
            animal.reduceEnergy(1);
        } else {
            if (nextPosition.getX() < 0
                    || nextPosition.getY() < 0
                    || nextPosition.getX() >= configuration.mapWidth()
                    || nextPosition.getY() >= configuration.mapHeight()) {
                var newPosition = new Vector2d(
                        random.nextInt(configuration.mapWidth()),
                        random.nextInt(configuration.mapHeight())
                );
                animal.reduceEnergy(configuration.energyNeedToReproduce());
                animal.setPosition(newPosition);
            } else {
                animal.setPosition(nextPosition);
                animal.reduceEnergy(1);
            }

        }

        place(animal);
    }

    public void setSelectedAnimal(Animal selectedAnimal) {
        this.selectedAnimal = selectedAnimal;
        draw(true);
    }
}
