package com.app.utils;

import com.app.models.Animal;
import com.app.models.IMapEntity;
import com.app.models.Plant;

import java.util.Comparator;

public class EntitiesComparator implements Comparator<IMapEntity> {
    @Override
    public int compare(IMapEntity o1, IMapEntity o2) {
        if (o1.equals(o2)) {
            return 0;
        }
        // both are plant
        if (o1.getClass() == o2.getClass() && o1.getClass().equals(Plant.class)) {
            return 0;
        }
        // both animals
        if (o1.getClass() == o2.getClass() && o1.getClass().equals(Animal.class)) {
            var difference = ((Animal) o2).getCurrentEnergy() - ((Animal) o1).getCurrentEnergy();
            return difference != 0 ? difference : -1;
        }
        // different
        if (o1.getClass().equals(Animal.class)) {
            return -1;
        }
        return 1;
    }
}
