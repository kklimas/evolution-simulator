package com.app;

import com.app.enums.variants.MapVariant;
import com.app.enums.variants.MutationVariant;
import com.app.enums.variants.PlantGrowVariant;

public class Configuration {
    private int mapWidth;
    private final int mapHeight;
    private final int startPlantsNumber;
    private final int plantEnergy;
    private final int newPlantsEveryDay;
    private final int animalsStartNumber;
    private final int startEnergy;
    private final int energyNeedToReproduce;
    private final int energyWastedDuringReproduction;
    private final int minimalMutationNumber;
    private final int maximalMutationNumber;
    private final int genomeLength;
    private final MapVariant mapVariant;
    private final PlantGrowVariant plantGrowVariant;
    private final MutationVariant mutationVariant;

    public Configuration(
            int mapWidth,
            int mapHeight,
            int startPlantsNumber,
            int plantEnergy,
            int newPlantsEveryDay,
            int animalsStartNumber,
            int startEnergy,
            int energyNeedToReproduce,
            int energyWastedDuringReproduction,
            int minimalMutationNumber,
            int maximalMutationNumber,
            int genomeLength,
            MapVariant mapVariant,
            PlantGrowVariant plantGrowVariant,
            MutationVariant mutationVariant) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.startPlantsNumber = startPlantsNumber;
        this.plantEnergy = plantEnergy;
        this.newPlantsEveryDay = newPlantsEveryDay;
        this.animalsStartNumber = animalsStartNumber;
        this.startEnergy = startEnergy;
        this.energyNeedToReproduce = energyNeedToReproduce;
        this.energyWastedDuringReproduction = energyWastedDuringReproduction;
        this.minimalMutationNumber = minimalMutationNumber;
        this.maximalMutationNumber = maximalMutationNumber;
        this.genomeLength = genomeLength;
        this.mapVariant = mapVariant;
        this.plantGrowVariant = plantGrowVariant;
        this.mutationVariant = mutationVariant;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getStartPlantsNumber() {
        return startPlantsNumber;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getNewPlantsEveryDay() {
        return newPlantsEveryDay;
    }

    public int getAnimalsStartNumber() {
        return animalsStartNumber;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getEnergyNeedToReproduce() {
        return energyNeedToReproduce;
    }

    public int getEnergyWastedDuringReproduction() {
        return energyWastedDuringReproduction;
    }

    public int getMinimalMutationNumber() {
        return minimalMutationNumber;
    }

    public int getMaximalMutationNumber() {
        return maximalMutationNumber;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public MapVariant getMapVariant() {
        return mapVariant;
    }

    public PlantGrowVariant getPlantGrowVariant() {
        return plantGrowVariant;
    }

    public MutationVariant getMutationVariant() {
        return mutationVariant;
    }
}
