package com.javarush.island.kalichinskaia.logic.predators;

import com.javarush.island.kalichinskaia.logic.Animal;
import com.javarush.island.kalichinskaia.logic.Organism;
import com.javarush.island.kalichinskaia.logic.island.Area;
import com.javarush.island.kalichinskaia.tmp.config.Config.Limit;

import java.util.Map;

public class Boa extends Animal {
    public Boa(Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }
}
