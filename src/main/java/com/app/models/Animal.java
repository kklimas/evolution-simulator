package com.app.models;

import com.app.configurations.CustomConfiguration;
import com.app.enums.MoveDirection;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractMapEntity {

    private final IWorldMap worldMap;

    private int currentEnergy;
    private MoveDirection direction = MoveDirection.N;
    private final List<Integer> genotype;
    private int currentGenomeIndex = 0;

    private final Random random = new Random();

    public Animal(CustomConfiguration configuration, IWorldMap worldMap) {
        this.configuration = configuration;
        this.worldMap = worldMap;
        this.genotype = generateGenotype();
        init();
    }

    public Animal(CustomConfiguration configuration, IWorldMap worldMap, List<Integer> genotype) {
        this.configuration = configuration;
        this.worldMap = worldMap;
        this.genotype = genotype;
        init();
    }

    public void move() {
        // rotation
        currentGenomeIndex = currentGenomeIndex % genotype.size();
        var rotation = this.genotype.get(currentGenomeIndex);
        currentGenomeIndex++;
        direction = direction.next(rotation);
        // move
        worldMap.move(this);
    }

    public MoveDirection getDirection() {
        return direction;
    }

    public void setDirection(MoveDirection direction) {
        this.direction = direction;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void reduceEnergy(int energy) {
        currentEnergy = currentEnergy >= energy ? currentEnergy - energy : 0;
    }

    @Override
    public Shape getShape() {
        var energyPercentage = currentEnergy * 1.0 / configuration.startEnergy();
        var color = energyPercentage < 1 ? energyPercentage : 1;
        return new Circle(10, Color.color(color, 0, 0));
    }

    private void init() {
        this.currentEnergy = configuration.startEnergy();
        selectPosition();
    }

    private List<Integer> generateGenotype() {
        List<Integer> genomes = new ArrayList<>();
        for (var i = 0; i < configuration.genomeLength(); i++) {
            var genome = random.nextInt(9);
            genomes.add(genome);
        }
        return genomes;
    }
}
