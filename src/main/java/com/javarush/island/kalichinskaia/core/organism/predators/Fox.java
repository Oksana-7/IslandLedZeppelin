package com.javarush.island.kalichinskaia.core.organism.predators;

import com.javarush.island.kalichinskaia.core.organism.Animal;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.config.Config.Limit;

import java.util.Map;

public class Fox extends Animal {
    public Fox(Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap,area);
    }
}
