package com.javarush.island.kalichinskaia.tmp.lifeprocess;

import com.javarush.island.kalichinskaia.logic.Organism;
import com.javarush.island.kalichinskaia.logic.island.Area;
import com.javarush.island.kalichinskaia.logic.island.Island;
import com.javarush.island.kalichinskaia.tmp.config.Config;
import lombok.Setter;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Setter
public abstract class AbstractLifeProcess implements Runnable {
    protected Config config;
    protected Island island;

    // todo via streams api
    protected void execute(Consumer<Organism> action) {
        Area[][] areas = island.getAreas();
        for (Area[] area : areas) {
            for (Area value : area) {
                // todo move logic to Area
                Map<String, Set<Organism>> allOrganisms = value.getOrganismsByType();
                for (Set<Organism> organisms : allOrganisms.values()) {
                    for (Organism organism : organisms) {
                        action.accept(organism);
                    }
                }
            }
        }
    }
}