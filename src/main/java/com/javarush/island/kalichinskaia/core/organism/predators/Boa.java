package com.javarush.island.kalichinskaia.core.organism.predators;

import com.javarush.island.kalichinskaia.core.organism.Animal;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.config.Config.Params;

import java.util.Map;

public class Boa extends Animal {
    public Boa(Params params, Map<String, Integer> foodMap, Area area) {
        super(params, foodMap, area);
    }
}
