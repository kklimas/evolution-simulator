package com.app.models;

import java.util.List;
import java.util.SortedSet;

public interface IWorldMap {

    void place(IMapEntity mapElement);

    void move(Animal animal);

    void removeAll(List<IMapEntity> entitiesList);

    List<Vector2d> getTakenPlaces();

    SortedSet<IMapEntity> objectsAt(Vector2d position);
}
