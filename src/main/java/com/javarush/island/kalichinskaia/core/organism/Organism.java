package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.config.Config.Params;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public abstract class Organism {
    private final Params param; // todo+ rename Params
    @Setter
    private double weight;
    private final Map<String, Integer> foodMap;
    @Setter
    private Area area;

    protected Organism(Params param, Map<String, Integer> foodMap, Area area) {
        this.param = param;
        this.weight = ThreadLocalRandom.current().nextDouble(param.getMaxWeight() / 2, param.getMaxWeight());
        this.foodMap = foodMap;
        this.area = area;
    }

    public Organism createChild() {
        Organism child = createOrganismOfType(this.getClass(), this.param, this.foodMap, this.area);
        child.setWeight(getChildWeight());
        return child;
    }


    public static Organism createOrganismOfType(Class<? extends Organism> orgType,
                                                Params limit, Map<String, Integer> foodMap, Area area) {
        try {
            return orgType.getConstructor(Params.class, Map.class, Area.class)
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
            getArea().addOrganism(child);
        }
    }

    public void move() {

    }


    protected boolean canReproduce() {
        Set<Organism> organisms = getArea().getOrganismsByType().get(getClass().getSimpleName());
        return weight > getParam().getMaxWeight() / 2
                && organisms.contains(this)
                && organisms.size() < param.getMaxCountInArea();
    }

    protected abstract double getChildWeight();

    public void changeWeight(double weight, double delta) {
        double newWeight = weight - delta;
        setWeight(newWeight);
    }

}