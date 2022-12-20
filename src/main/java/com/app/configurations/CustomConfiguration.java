package com.app.configurations;

import com.app.enums.variants.MapVariant;
import com.app.enums.variants.MutationVariant;
import com.app.enums.variants.PlantGrowVariant;

public record CustomConfiguration(int mapWidth, int mapHeight, int startPlantsNumber, int plantEnergy,
                                  int newPlantsEveryDay, int animalsStartNumber, int startEnergy,
                                  int energyNeedToReproduce, int energyWastedDuringReproduction,
                                  int minimalMutationNumber, int maximalMutationNumber, int genomeLength,
                                  MapVariant mapVariant, PlantGrowVariant plantGrowVariant,
                                  MutationVariant mutationVariant) {
}
