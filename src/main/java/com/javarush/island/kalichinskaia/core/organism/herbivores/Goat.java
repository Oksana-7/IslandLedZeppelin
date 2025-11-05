package com.javarush.island.kalichinskaia.core.organism.herbivores;

import com.javarush.island.kalichinskaia.core.organism.Animal;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.config.Config.Limit;

import java.util.Map;

public class Goat extends Animal {
    public Goat(Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }
}
