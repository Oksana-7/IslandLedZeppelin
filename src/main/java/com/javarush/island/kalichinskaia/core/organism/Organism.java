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
    private final Params params;
    @Setter
    private double weight;
    private final Map<String, Integer> foodMap;
    @Setter
    private Area area;

    private String icon = "?";
    private String letter = "?";

    protected Organism(Params params, Map<String, Integer> foodMap, Area area) {
        this.params = params;
        this.weight = ThreadLocalRandom.current().nextDouble(params.getMaxWeight() / 2, params.getMaxWeight());
        this.foodMap = foodMap;
        this.area = area;
    }

    public Organism createChild() {
        Organism child = createOrganismOfType(this.getClass(), this.params, this.foodMap, this.area);
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
        return weight > getParams().getMaxWeight() / 2
                && organisms.contains(this)
                && organisms.size() < params.getMaxCountInArea();
    }

    protected abstract double getChildWeight();

    public void changeWeight(double delta) {
        weight += delta;
        weight = Math.max(0, weight);
        weight = Math.min(weight, params.getMaxWeight());
    }

    public String getIcon() {
        update();
        return icon;
    }

    public String getLetter() {
        update();
        return letter;
    }

    private void update() {
        if (params == null) {
            if (!getArea().getOrganismsByType().get(getClass().getSimpleName()).isEmpty()) {
                Organism organism = getArea().getOrganismsByType().get(getClass().getSimpleName())
                        .iterator()
                        .next();
                params = organism.getParams();//not final?
                icon = organism.getIcon();
                letter = organism.getLetter();
            }
        }
    }

}