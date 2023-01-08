package com.app.configurations;

import com.app.enums.variants.AnimalBehaveVariant;
import com.app.enums.variants.MapVariant;
import com.app.enums.variants.MutationVariant;
import com.app.enums.variants.PlantGrowVariant;

public class DefaultConfiguration {
    public static final String TITLE = "Evolution simulation";

    public static final int DELAY = 200;
    public static final int INIT_WINDOW_WIDTH = 800;
    public static final int INIT_WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1500;
    public static final int WINDOW_HEIGHT = 1000;
    public static final int CHARTS_HEIGHT = 200;
    public static final int INFO_HEIGHT = 500;
    public static final double EQUATOR_PERCENTAGE_HEIGHT = 0.2;

    // default properties if no provided by user
    public static final int D_MAP_WIDTH = 50;
    public static final int D_MAP_HEIGHT = 30;
    public static final int D_START_PLANTS_NUMBER = 10;
    public static final int D_PLANTS_ENERGY = 10;
    public static final int D_NEW_PLANTS_EVERY_DAY = 10;
    public static final int D_ANIMALS_START_NUMBER = 5;
    public static final int D_START_ENERGY = 100;
    public static final int D_ENERGY_NEED_TO_REPRODUCE = 50;
    public static final int D_ENERGY_WASTED_DURING_REPRODUCTION = 20;
    public static final int D_MINIMAL_MUTATION_NUMBER = 0;
    public static final int D_MAXIMAL_MUTATION_NUMBER = 5;
    public static final int D_GENOME_LENGTH = 8;
    public static final MapVariant D_MAP_VARIANT = MapVariant.GLOBE;
    public static final PlantGrowVariant D_PLANT_GROW_VARIANT = PlantGrowVariant.FORESTED_AREA;
    public static final MutationVariant D_MUTATION_VARIANT = MutationVariant.RANDOM;
    public static final AnimalBehaveVariant D_ANIMAL_BEHAVE_VARIANT = AnimalBehaveVariant.WELL_BRED;

    private DefaultConfiguration() {}
}
