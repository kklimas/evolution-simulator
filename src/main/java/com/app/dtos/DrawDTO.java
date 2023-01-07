package com.app.dtos;

import com.app.entities.IMapEntity;

import java.util.List;

public record DrawDTO(Integer currentDay, List<IMapEntity> entitiesToDraw, Integer animalsNumber,
                      Integer averageEnergy, Integer averageAnimalLifespan,
                      Integer plantsNumber, Integer freePlaces,
                      Integer deadAnimals, String dominantGenotype) {
}