package com.javarush.island.kalichinskaia.logic.island;

import com.javarush.island.kalichinskaia.logic.Animal;
import com.javarush.island.kalichinskaia.logic.Organism;
import com.javarush.island.kalichinskaia.logic.Plant;
import com.javarush.island.kalichinskaia.tmp.config.Config;
import com.javarush.island.kalichinskaia.tmp.config.Config.Limit;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.javarush.island.kalichinskaia.tmp.config.Config.TYPES;

@Getter
// todo add method: public Set<Organism> getOrganismsSet() {...}
public class Area {

    private final ConcurrentHashMap<String, Set<Organism>> organismsByType; // todo should getter be synchronized???
    @Setter
    private List<Area> neighborAreas = new ArrayList<>();

    public Area(Config config) {
        organismsByType = new ConcurrentHashMap<>();
        for (Class<? extends Organism> type : TYPES) {
            String animalName = type.getSimpleName();
            organismsByType.put(animalName, new HashSet<>());
            boolean isFill = ThreadLocalRandom.current().nextDouble(0d, 100d) < config.getPercentProbably();
            if (isFill) {
                int max = config.getAnimalLimits().get(animalName).getMaxCountInArea();
                int count = ThreadLocalRandom.current().nextInt(0, max);
                for (int j = 0; j < count; j++) {
                    Limit limit = config.getAnimalLimits().get(animalName);
                    Map<String, Integer> foodMap = config.getFoodMap().get(animalName);
                    Organism organism = Organism.createOrganismOfType(type, limit, foodMap, this);
                    organismsByType.get(animalName).add(organism);
                }
            }
        }
    }

    public Area getTargetArea(int countStep) {
        Set<Area> visitedAreas = new HashSet<>();
        Area currentArea = this;
        while (visitedAreas.size() < countStep) {
            var neighborAreas = currentArea
                    .neighborAreas
                    .stream()
                    .filter(area -> !visitedAreas.contains(area))
                    .toList();
            int countDirections = neighborAreas.size();
            if (countDirections == 0) break;
            int index = ThreadLocalRandom.current().nextInt(0, countDirections);
            currentArea = neighborAreas.get(index);
            visitedAreas.add(currentArea);
        }
        return currentArea;
    }

    public synchronized boolean addOrganism(Organism organism) {
        Set<Organism> organisms = organismsByType.get(getClass().getSimpleName());
        int maxCount = organism.getLimit().getMaxCountInArea();
        int size = organisms.size();
        if (size >= maxCount) return false;
        return organisms.add(organism);
    }

    public synchronized boolean deleteOrganism(Organism organism) {
        Set<Organism> organisms = organismsByType.get(organism.getClass().getSimpleName());
        return organisms.remove(organism);
    }
}
