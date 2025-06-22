package com.javarush.island.kalichinskaia.logic.herbivores;

import com.javarush.island.kalichinskaia.logic.Animal;
import com.javarush.island.kalichinskaia.logic.Organism;
import com.javarush.island.kalichinskaia.logic.island.Area;
import com.javarush.island.kalichinskaia.tmp.config.Config.Limit;

import java.util.Map;

public class Caterpillar extends Animal {
    public Caterpillar(Limit limit, Map<String, Integer> foodMap, Area area) { super(limit, foodMap, area);
    }
}
