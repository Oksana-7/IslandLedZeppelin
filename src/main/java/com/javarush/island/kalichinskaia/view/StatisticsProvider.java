package com.javarush.island.kalichinskaia.view;

import com.javarush.island.kalichinskaia.core.habitat.Island;
import com.javarush.island.kalichinskaia.core.organism.Organism;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class StatisticsProvider {

    private final Island island;

    private Map<Organism, Long> statistics = new HashMap<>();

    public Map<Organism, Long> getStatistics() {
        updateStatistics();
        return statistics;
    }

    private void updateStatistics() {
        // todo impl
    }
}
