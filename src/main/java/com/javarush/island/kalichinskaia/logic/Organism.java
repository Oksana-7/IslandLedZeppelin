package com.javarush.island.kalichinskaia.logic;

import com.javarush.island.kalichinskaia.logic.island.Area;
import com.javarush.island.kalichinskaia.tmp.config.Config.Limit;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public abstract class Organism {
    private final Limit limit;
    @Setter
    private double weight;
    private final Map<String, Integer> foodMap;
    @Setter
    private Area area;

    protected Organism(Limit limit, Map<String, Integer> foodMap, Area area) {
        this.limit = limit;
        this.weight = ThreadLocalRandom.current().nextDouble(limit.getMaxWeight() / 2, limit.getMaxWeight());
        this.foodMap = foodMap;
        this.area = area;
    }

    public Organism createChild() {
        Organism child = createOrganismOfType(this.getClass(), this.limit, this.foodMap, this.area);
        child.setWeight(getChildWeight());
        return child;
    }


    public static Organism createOrganismOfType(Class<? extends Organism> orgType,
                                                Limit limit, Map<String, Integer> foodMap, Area area) {
        try {
            return orgType.getConstructor(Limit.class, Map.class, Area.class)
                    .newInstance(limit, foodMap, area);
        } catch (Exception e) {
            throw new RuntimeException("not found Entity constructor", e);
        }
    }

    public void eatAndGrow() {
    }

    public void reproduce() {
        synchronized (getArea()) {
            if (!canReproduce()) return;
            Organism child = createChild();
            Set<Organism> organisms = getArea().getOrganismsByType().get(getClass().getSimpleName());
            organisms.add(child);
        }
    }

    public void move() {

    }

    protected boolean canReproduce() {
        Set<Organism> organisms = getArea().getOrganismsByType().get(getClass().getSimpleName());
        return weight <= getLimit().getMaxWeight() / 2
                && organisms.contains(this)
                && organisms.size() < limit.getMaxCountInArea();
    }

    protected abstract double getChildWeight();

}