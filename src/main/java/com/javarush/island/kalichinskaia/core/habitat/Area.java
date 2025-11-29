package com.javarush.island.kalichinskaia.core.habitat;

import com.javarush.island.kalichinskaia.core.organism.Organism;
import com.javarush.island.kalichinskaia.config.Config;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.javarush.island.kalichinskaia.config.Config.TYPES;

@Getter
public class Area {

    // todo проверить в самом конце, используется ли конкурентный доступ к этой мапе (из разных потоков).
    //  может достаточно обычной HashMap, которая более производительная
    private final ConcurrentHashMap<String, Set<Organism>> organismsByType;
    @Setter
    private List<Area> neighborAreas = new ArrayList<>();

    public Area(Config config) {
        organismsByType = new ConcurrentHashMap<>();
        for (Class<? extends Organism> type : TYPES) {
            String organismName = type.getSimpleName();
            organismsByType.put(organismName, new HashSet<>());
            boolean isFill = ThreadLocalRandom.current().nextDouble(100d) < config.getPercentProbably();
            if (isFill) {
                int max = config.getOrganismParams().get(organismName).getMaxCountInArea();
                int count = ThreadLocalRandom.current().nextInt(max);
                for (int j = 0; j < count; j++) {
                    Config.Params limit = config.getOrganismParams().get(organismName);
                    Map<String, Integer> foodMap = config.getFoodMap().get(organismName);
                    Organism organism = Organism.createOrganismOfType(type, limit, foodMap, this);
                    organismsByType.get(organismName).add(organism);
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

    public synchronized Set<Organism> getAllOrganisms() {
        return getOrganismsByType().values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public synchronized boolean addOrganism(Organism organism) {
        Set<Organism> organisms = organismsByType.get(getClass().getSimpleName());
        int maxCount = organism.getParam().getMaxCountInArea();
        int size = organisms.size();
        if (size >= maxCount) return false;
        return organisms.add(organism);
    }

    public synchronized boolean removeOrganism(Organism organism) {
        Set<Organism> organisms = organismsByType.get(organism.getClass().getSimpleName());
        return organisms.remove(organism);
    }
}
