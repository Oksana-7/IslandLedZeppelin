package com.javarush.island.kalichinskaia.view;

import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.core.habitat.Island;
import com.javarush.island.kalichinskaia.core.organism.Organism;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class StatisticsProvider {

    private final Island island;

    private Map<String, Long> counts = new HashMap<>();

    @RequiredArgsConstructor
    public static class Stat {
        private final long count;
        private final long delta;

        @Override
        public String toString() {
            return String.format("%d(%d)", count, delta);
        }
    }

    public Map<String, Stat> getStatistics() {
        Map<String, Stat> statistics = new HashMap<>();
        Map<String, Long> newCounts = new HashMap<>();
        Area[][] areas = island.getAreas();
        for (Area[] row : areas) {
            for (Area area : row) {
                Map<String, Set<Organism>> organismsByType = area.getOrganismsByType();
                for (Map.Entry<String, Set<Organism>> entry : organismsByType.entrySet()) {
                    String organism = entry.getKey();
                    long count = entry.getValue().size();
                    newCounts.put(organism, newCounts.getOrDefault(organism, 0L) + count);
                }
            }
        }

        for (Map.Entry<String, Long> entry : newCounts.entrySet()) {
            String organism = entry.getKey();
            long newCount = entry.getValue();
            statistics.put(organism, new Stat(newCount, newCount - counts.getOrDefault(organism, 0L)));
        }
        counts = newCounts;
        return statistics;
    }
}
