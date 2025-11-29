package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.config.Config.Params;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Plant extends Organism {

    protected Plant(Params limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }

    @Override
    public void eatAndGrow() {
        synchronized (getArea()) {
            int growPercent = ThreadLocalRandom.current().nextInt(getParams().getAdditional().get("maxGrowPercent"));
            double grow = getWeight() * growPercent / 100.0;
            changeWeight(-grow);
        }
    }

    @Override
    protected double getChildWeight() {
        return getWeight() / 10;
    }

}
