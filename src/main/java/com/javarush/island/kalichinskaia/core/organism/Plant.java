package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.config.Config;

import java.util.Map;

public class Plant extends Organism {

    protected Plant(Config.Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }

    @Override
    public void eatAndGrow() {
        // todo skip eat
        // todo impl grow
        super.eatAndGrow();
    }

    @Override
    protected double getChildWeight() {
        return getWeight() / 10;
    }

}
