package com.javarush.island.kalichinskaia.core.action;

import com.javarush.island.kalichinskaia.core.organism.Organism;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.core.habitat.Island;
import lombok.Setter;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

@Setter
public abstract class Action implements Callable<Void> {

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