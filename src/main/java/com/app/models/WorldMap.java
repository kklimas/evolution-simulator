package com.app.models;

import com.app.utils.EntitiesComparator;

import java.util.*;

public class WorldMap {
    private Map<Vector2d, SortedSet<IMapEntity>> entities = new HashMap<>();

    public void place(IMapEntity entity) {
        if (entities.containsKey(entity.getPosition())) {
            entities.get(entity.getPosition()).add(entity);
        } else {
            var treeSet = new TreeSet<>(new EntitiesComparator());
            treeSet.add(entity);
            entities.put(entity.getPosition(), treeSet);
        }
    }
    public void removeAll(List<IMapEntity> entitiesList) {
        entitiesList.forEach(entity -> entities.get(entity.getPosition()).remove(entity));
    }
}
