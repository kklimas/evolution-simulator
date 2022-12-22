package com.app.models;

import com.app.configurations.CustomConfiguration;
import com.app.enums.MoveDirection;
import com.app.enums.variants.MapVariant;
import com.app.utils.EntitiesComparator;

import java.util.*;

public class WorldMap {
    private final CustomConfiguration configuration;
    private final Map<Vector2d, SortedSet<IMapEntity>> entities;
    private final Random random = new Random();

    private int deadCount = 0;

    public WorldMap(CustomConfiguration configuration) {
        this.configuration = configuration;
        this.entities = new HashMap<>();
    }

    public boolean place(IMapEntity entity) {
        if (entities.containsKey(entity.getPosition())) {
            if (entity.getClass().equals(Plant.class)
                    && entities.get(entity.getPosition()).last().getClass().equals(Plant.class)) {
                return false;
            }
            return entities.get(entity.getPosition()).add(entity);
        } else {
            var treeSet = new TreeSet<>(new EntitiesComparator());
            var result = treeSet.add(entity);
            entities.put(entity.getPosition(), treeSet);
            return result;
        }
    }

    public void move(Animal animal) {

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
            var leftBottom = new Vector2d(-1, -1);
            var rightTop = new Vector2d(configuration.mapWidth(), configuration.mapHeight());

            if (animal.getPosition().precedes(leftBottom) || animal.getPosition().follows(rightTop)) {
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

    public void eat(Animal animal) {
        remove(animal);
        place(animal);
    }


    public void removeAll(List<IMapEntity> entitiesList) {
        entitiesList.forEach(this::remove);
    }


    public Set<Vector2d> getTakenPlaces() {
        return entities.keySet();
    }


    public SortedSet<IMapEntity> objectsAt(Vector2d position) {
        return entities.get(position);
    }

    private void remove(IMapEntity entity) {
        var set = entities.get(entity.getPosition());
        if (set != null) {
            set.remove(entity);
            if (set.isEmpty()) {
                entities.remove(entity.getPosition());
            }
        } else {
            System.out.println("Error");
        }

    }

    public int getDeadCount() {
        return deadCount;
    }

    public void increaseDeadCount(int count) {
        deadCount += count;
    }
}
