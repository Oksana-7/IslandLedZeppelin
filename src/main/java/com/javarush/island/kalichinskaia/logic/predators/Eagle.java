package com.javarush.island.kalichinskaia.logic.predators;

import com.javarush.island.kalichinskaia.logic.Animal;
import com.javarush.island.kalichinskaia.logic.island.Area;
import com.javarush.island.kalichinskaia.tmp.config.Config.Limit;

import java.util.Map;

public class Eagle extends Animal {
    public Eagle(Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }
}
