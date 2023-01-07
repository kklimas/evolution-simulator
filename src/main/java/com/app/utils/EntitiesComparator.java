package com.app.utils;

import com.app.entities.Animal;
import com.app.entities.IMapEntity;
import com.app.entities.Plant;

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
            var a1 = (Animal) o1;
            var a2 = (Animal) o2;
            var energyDifference = a2.getCurrentEnergy() - a1.getCurrentEnergy();
            if (energyDifference != 0) {
                return energyDifference;
            }
            var lifespanDifference = a2.getDaysLiving() - a1.getDaysLiving();
            if (lifespanDifference != 0) {
                return lifespanDifference;
            }
            var childrenDifference = a2.getChildrenNumber() - a1.getChildrenNumber();
            if (childrenDifference != 0) {
                return childrenDifference;
            }
            return -1;
        }
        // different
        if (o1.getClass().equals(Animal.class)) {
            return -1;
        }
        return 1;
    }
}
