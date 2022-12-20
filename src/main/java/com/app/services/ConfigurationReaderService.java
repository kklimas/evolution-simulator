package com.app.services;

import com.app.configurations.CustomConfiguration;
import com.app.enums.variants.MapVariant;
import com.app.enums.variants.MutationVariant;
import com.app.enums.variants.PlantGrowVariant;
import com.app.exceptions.InvalidConfigurationFileException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.app.configurations.DefaultConfiguration.*;

public class ConfigurationReaderService {

    private static final String MAP_WIDTH = "mapWidth";
    private static final String MAP_HEIGHT = "mapHeight";
    private static final String START_PLANTS_NUMBER = "startPlantsNumber";
    private static final String PLANT_ENERGY = "plantEnergy";
    private static final String NEW_PLANTS_EVERY_DAY = "newPlantsEveryDay";
    private static final String START_ANIMALS_NUMBER = "animalsStartNumber";
    private static final String START_ENERGY = "startEnergy";
    private static final String ENERGY_NEED_TO_REPRODUCE = "energyNeedToReproduce";
    private static final String ENERGY_WASTED_DURING_REPRODUCTION = "energyWastedDuringReproduction";
    private static final String MINIMAL_MUTATION_NUMBER = "minimalMutationNumber";
    private static final String MAXIMAL_MUTATION_NUMBER = "maximalMutationNumber";
    private static final String GENOME_LENGTH = "genomeLength";
    private static final String MAP_VARIANT = "mapVariant";
    private static final String PLANT_GROW_VARIANT = "plantGrowVariant";
    private static final String MUTATION_VARIANT = "mutationVariant";

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private ConfigurationReaderService() {}

    public static CustomConfiguration getConfigurationFromFile(File file) throws InvalidConfigurationFileException {
        var properties = getPropertiesFromFile(file);
        return assignPropertiesToConfiguration(properties);
    }

    public static CustomConfiguration getDefaultConfiguration() {
        return new CustomConfiguration(
                D_MAP_WIDTH,
                D_MAP_HEIGHT,
                D_START_PLANTS_NUMBER,
                D_PLANTS_ENERGY,
                D_NEW_PLANTS_EVERY_DAY,
                D_ANIMALS_START_NUMBER,
                D_START_ENERGY,
                D_ENERGY_NEED_TO_REPRODUCE,
                D_ENERGY_WASTED_DURING_REPRODUCTION,
                D_MINIMAL_MUTATION_NUMBER,
                D_MAXIMAL_MUTATION_NUMBER,
                D_GENOME_LENGTH,
                D_MAP_VARIANT,
                D_PLANT_GROW_VARIANT,
                D_MUTATION_VARIANT
        );
    }

    private static HashMap<String, Integer> getPropertiesFromFile(File file) throws InvalidConfigurationFileException {
        var properties = new HashMap<String, Integer>();
        if (file == null) {
            throw new InvalidConfigurationFileException("Provided file was not found.", false);
        }
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                var split = line.split("=");
                if (split.length != 2 || !pattern.matcher(split[1]).matches()) throw new InvalidConfigurationFileException(file.getName());

                properties.put(split[0], Integer.parseInt(split[1]));

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static CustomConfiguration assignPropertiesToConfiguration(HashMap<String, Integer> properties) throws InvalidConfigurationFileException {
        var mapWidth = properties.get(MAP_WIDTH);
        var mapHeight = properties.get(MAP_HEIGHT);
        var startPlantsNumber = properties.get(START_PLANTS_NUMBER);
        var plantEnergy = properties.get(PLANT_ENERGY);
        var newPlantsEveryDay = properties.get(NEW_PLANTS_EVERY_DAY);
        var animalsStartNumber = properties.get(START_ANIMALS_NUMBER);
        var startEnergy = properties.get(START_ENERGY);
        var energyNeedToReproduce = properties.get(ENERGY_NEED_TO_REPRODUCE);
        var energyWastedDuringReproduction = properties.get(ENERGY_WASTED_DURING_REPRODUCTION);
        var minimalMutationNumber = properties.get(MINIMAL_MUTATION_NUMBER);
        var maximalMutationNumber = properties.get(MAXIMAL_MUTATION_NUMBER);
        var genomeLength = properties.get(GENOME_LENGTH);

        var v1 = properties.get(MAP_VARIANT);
        var v2 = properties.get(PLANT_GROW_VARIANT);
        var v3 = properties.get(MUTATION_VARIANT);
        if (validateVariant(v1) || validateVariant(v2) || validateVariant(v3))
            throw new InvalidConfigurationFileException("Application variants are invalid.", false);

        var props = new ArrayList<Object>(List.of(mapWidth,
                mapHeight,
                startPlantsNumber,
                plantEnergy,
                newPlantsEveryDay,
                animalsStartNumber,
                startEnergy,
                energyNeedToReproduce,
                energyWastedDuringReproduction,
                minimalMutationNumber,
                maximalMutationNumber,
                genomeLength,
                v1,
                v2,
                v3));
        if (!props.stream().allMatch(Objects::nonNull))
            throw new InvalidConfigurationFileException("Properties names are invalid.", false);


        var mapVariant = MapVariant.values()[v1];
        var plantGrowVariant = PlantGrowVariant.values()[v2];
        var mutationVariant = MutationVariant.values()[v3];


        return new CustomConfiguration(
                mapWidth,
                mapHeight,
                startPlantsNumber,
                plantEnergy,
                newPlantsEveryDay,
                animalsStartNumber,
                startEnergy,
                energyNeedToReproduce,
                energyWastedDuringReproduction,
                minimalMutationNumber,
                maximalMutationNumber,
                genomeLength,
                mapVariant,
                plantGrowVariant,
                mutationVariant
        );
    }

    private static boolean validateVariant(int number) {
        return number <= -1 || number >= 2;
    }
}
