package com.javarush.island.kalichinskaia.core.action;

import com.javarush.island.kalichinskaia.core.organism.Organism;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.core.habitat.Island;
import lombok.Setter;

import java.util.function.Consumer;

@Setter
public abstract class Action implements Runnable {

    protected Island island;

    protected void execute(Consumer<Organism> action) {
        Area[][] areas = island.getAreas();
        for (Area[] areaRow : areas) {
            for (Area area : areaRow) {
                for (Organism organism : area.getAllOrganisms()) {
                    action.accept(organism);
                }
            }
        }
    }
}