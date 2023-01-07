package com.app.models;

public record CSVRecord(Integer day, Integer animalsNumber, Integer averageEnergy, Integer animalLifeExpectancy,
                        Integer plantsNumber, Integer freePlaces, Integer deadAnimals) {
    @Override
    public String toString() {
        return day +
                "," + animalsNumber +
                "," + averageEnergy +
                "," + animalLifeExpectancy +
                "," + plantsNumber +
                "," + freePlaces +
                "," + deadAnimals;
    }
}
