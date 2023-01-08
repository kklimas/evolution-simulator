package com.app.entities;

import com.app.configurations.CustomConfiguration;
import com.app.enums.MoveDirection;
import com.app.enums.variants.AnimalBehaveVariant;
import com.app.enums.variants.MutationVariant;
import com.app.models.Vector2d;
import com.app.utils.Engine;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractMapEntity {

    private final Engine engine;
    private int currentEnergy;
    private MoveDirection direction = MoveDirection.N;
    private final List<Integer> genotype;
    private int currentGenomeIndex = 0;
    private int childrenNumber = 0;
    private int daysLiving = 0;
    private int dayOfDeath = -1;
    private boolean readyForReproduction = true;

    private final Random random = new Random();

    public Animal(CustomConfiguration configuration, Engine engine) {
        this.engine = engine;
        this.configuration = configuration;
        this.currentGenomeIndex = random.nextInt(configuration.genomeLength());
        this.genotype = generateGenotype();
        init();
    }

    public Animal(CustomConfiguration configuration, Engine engine, Vector2d currentPosition, List<Integer> genotype) {
        this.engine = engine;
        this.configuration = configuration;
        this.currentGenomeIndex = random.nextInt(configuration.genomeLength());
        this.genotype = genotype;
        this.position = currentPosition;
        this.currentEnergy = configuration.energyWastedDuringReproduction() * 2;
    }

    public void move() {
        // rotation
        currentGenomeIndex = currentGenomeIndex % genotype.size();
        var rotation = this.genotype.get(currentGenomeIndex);
        direction = direction.next(rotation);

        if (AnimalBehaveVariant.WELL_BRED == configuration.animalBehaveVariant()) {
            currentGenomeIndex++;
        } else {
            var choice = random.nextInt(5);
            if (choice < 4) {
                currentGenomeIndex++;
            } else {
                currentGenomeIndex = random.nextInt(configuration.genomeLength());
            }
        }

        // move
        readyForReproduction = true;
        daysLiving++;
    }

    public void eat() {
        currentEnergy += configuration.plantEnergy();
    }

    public void reproduce() {
        currentEnergy -= configuration.energyWastedDuringReproduction();
        childrenNumber++;
        readyForReproduction = false;
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
        var color = energyPercentage < 1 && energyPercentage >= 0 ? energyPercentage : 1;
        var shape = new Circle(getTileSize() / 2, Color.color(color, 0, 0));
        shape.setOnMouseClicked(event -> engine.setSelectedAnimal(this));
        shape.setCursor(Cursor.HAND);
        return shape;
    }

    public Animal getChild(Animal parent2) {
        // calculate genotype
        List<Integer> genes = new ArrayList<>();
        double totalEnergy = (this.getCurrentEnergy() + parent2.getCurrentEnergy()) * 1.0;

        double p1Indicator = (this.getCurrentEnergy() / totalEnergy) * configuration.genomeLength();
        double p2Indicator = (parent2.getCurrentEnergy() / totalEnergy) * configuration.genomeLength();
        int p1GensNumber = (int) Math.floor(p1Indicator);
        int p2GensNumber = (int) Math.ceil(p2Indicator);
        var side = random.nextInt(2);

        if (p1GensNumber == 0) {
            genes.addAll(parent2.getGenotype());
        } else if (p2GensNumber == 0) {
            genes.addAll(this.getGenotype());
        } else if (side == 0) {
            var parent1CutGenotype = this.getGenotype().subList(0, p1GensNumber);
            var parent2CutGenotype = parent2.getGenotype().subList(p1GensNumber, p1GensNumber + p2GensNumber);

            genes.addAll(parent1CutGenotype);
            genes.addAll(parent2CutGenotype);
        } else {
            var parent2CutGenotype = this.getGenotype().subList(0, p2GensNumber);
            var parent1CutGenotype = parent2.getGenotype().subList(p2GensNumber, p1GensNumber + p2GensNumber);

            genes.addAll(parent2CutGenotype);
            genes.addAll(parent1CutGenotype);
        }
        // mutation
        var mutatedGenes = random
                .nextInt((configuration.maximalMutationNumber() - configuration.minimalMutationNumber()) + 1) + configuration.minimalMutationNumber();
        for (var i = 0; i < mutatedGenes; i++) {
            var chosenGeneIndex = random.nextInt( genes.size());

            if (MutationVariant.RANDOM == configuration.mutationVariant()) {
                genes.set(chosenGeneIndex, random.nextInt(9));
            } else {
                var gen = genes.get(chosenGeneIndex);
                var value = random.nextInt() - 0.5 > 0 ? 1: -1;
                gen += value;
                if (gen > 8) gen = 0;
                if (gen < 0) gen = 8;
                genes.set(chosenGeneIndex, gen);
            }
            genes.set(chosenGeneIndex, random.nextInt(9));
        }
        return new Animal(configuration, engine, this.getPosition(), genes);
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

    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    public List<Integer> getGenotype() {
        return genotype;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public int getDaysLiving() {
        return daysLiving;
    }

    public boolean isReadyForReproduction() {
        return readyForReproduction;
    }

    public void setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }
}
