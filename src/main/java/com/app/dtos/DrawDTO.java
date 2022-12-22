package com.app.dtos;

import com.app.models.IMapEntity;

import java.util.List;

public record DrawDTO(Integer currentDay, List<IMapEntity> entitiesToDraw, Integer animalsNumber, Integer averageEnergy,
                      Integer plantsNumber, Integer freePlaces,
                      Integer deadAnimals, String dominantGenotype) {
}