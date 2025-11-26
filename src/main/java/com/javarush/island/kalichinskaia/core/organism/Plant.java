package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.config.Config.Limit;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Plant extends Organism {

    protected Plant(Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }

    @Override
    public void eatAndGrow() {
        synchronized (getArea()) {
            int growPercent = ThreadLocalRandom.current().nextInt(getLimit().getAdditional().get("maxGrowPercent"));
            // todo impl change weight via universal method (universal method in Organism "changeWeight")
            double grow = getWeight() * growPercent / 100.0;
            double newWeight = Math.max(getLimit().getMaxWeight(), getWeight() + grow);
            setWeight(newWeight);
        }
    }

    @Override
    protected double getChildWeight() {
        return getWeight() / 10;
    }

}
