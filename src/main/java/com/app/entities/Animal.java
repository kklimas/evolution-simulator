package com.app.entities;

import com.app.configurations.CustomConfiguration;
import com.app.enums.MoveDirection;
import com.app.models.Vector2d;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractMapEntity {

    private int currentEnergy;
    private MoveDirection direction = MoveDirection.N;
    private final List<Integer> genotype;
    private int currentGenomeIndex = 0;
    private int childrenNumber = 0;
    private int daysLiving = 0;
    private boolean readyForReproduction = true;

    private final Random random = new Random();

    public Animal(CustomConfiguration configuration) {
        this.configuration = configuration;
        this.genotype = generateGenotype();
        init();
    }

    public Animal(CustomConfiguration configuration, Vector2d currentPosition, List<Integer> genotype) {
        this.configuration = configuration;
        this.genotype = genotype;
        this.position = currentPosition;
        this.currentEnergy = configuration.energyWastedDuringReproduction() * 2;
    }

    public void move() {
        // rotation
        currentGenomeIndex = currentGenomeIndex % genotype.size();
        var rotation = this.genotype.get(currentGenomeIndex);
        currentGenomeIndex++;
        direction = direction.next(rotation);
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
        var color = energyPercentage < 1 ? energyPercentage : 1;
        return new Circle(getTileSize() / 2, Color.color(color, 0, 0));
    }

    public Animal getChild(Animal parent2) {
        // calculate genotype
        List<Integer> genes = new ArrayList<>();
        double totalEnergy = (this.getCurrentEnergy() + parent2.getCurrentEnergy()) * 1.0;

        double p1Indicator = (this.getCurrentEnergy() / totalEnergy) * configuration.genomeLength();
        double p2Indicator = (parent2.getCurrentEnergy() / totalEnergy) * configuration.genomeLength();
        int p1GensNumber = (int) Math.floor(p1Indicator);
        int p2GensNumber = (int) Math.ceil(p2Indicator);
        var side = (int) Math.round(Math.random());

        if (side == 0) {
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
            genes.set(chosenGeneIndex, random.nextInt(9));
        }
        return new Animal(configuration, this.getPosition(), genes);
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
}
