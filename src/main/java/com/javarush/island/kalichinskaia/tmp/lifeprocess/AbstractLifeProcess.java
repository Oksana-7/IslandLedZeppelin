package com.javarush.island.kalichinskaia.tmp.lifeprocess;

import com.javarush.island.kalichinskaia.logic.Organism;
import com.javarush.island.kalichinskaia.logic.island.Area;
import com.javarush.island.kalichinskaia.logic.island.Island;
import com.javarush.island.kalichinskaia.tmp.config.Config;
import lombok.Setter;

import java.util.function.Consumer;

@Setter
public abstract class AbstractLifeProcess implements Runnable {
    protected Config config;
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