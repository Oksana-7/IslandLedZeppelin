package com.javarush.island.kalichinskaia.logic;

import com.javarush.island.kalichinskaia.logic.island.Area;
import com.javarush.island.kalichinskaia.tmp.config.Config.Limit;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Organism {

    protected Animal(Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }

    @Override
    public void move() {
        // todo refactoring: move logic to this method from Area
        int maxCountStep = getLimit().getMaxSpeed();
        int countStep = ThreadLocalRandom.current().nextInt(maxCountStep + 1);
        Area targetArea = getArea().getTargetArea(countStep);
        targetArea.addOrganism(this);
        if (getArea().deleteOrganism(this)) setArea(targetArea);
        else targetArea.deleteOrganism(this);
    }

    public void moveV2() {
        int maxCountStep = getLimit().getMaxSpeed();
        int countStep = ThreadLocalRandom.current().nextInt(maxCountStep + 1);
        Area targetArea = getArea().getTargetArea(countStep);
        synchronized (targetArea) {
            Set<Organism> organisms = targetArea.getOrganismsByType().get(getClass().getSimpleName());
            int maxCount = getLimit().getMaxCountInArea();
            int size = organisms.size();
            if (size >= maxCount) return;
            organisms.add(this);
            synchronized (getArea()) {
                Set<Organism> organismsOfCurrentArea = getArea().getOrganismsByType().get(getClass().getSimpleName());
                if (organismsOfCurrentArea.remove(this)) {
                    setArea(targetArea);
                } else {
                    organisms.remove(this);
                }
            }
        }
    }

    public void moveV3() {
        int maxCountStep = getLimit().getMaxSpeed();
        int countStep = ThreadLocalRandom.current().nextInt(maxCountStep + 1);
        Area targetArea = getArea().getTargetArea(countStep);
        addTo(targetArea);
        if (deleteFrom(getArea())) setArea(targetArea);
        else deleteFrom(targetArea);
    }

    private void addTo(Area area) {
        synchronized (area) {
            Set<Organism> organisms = area.getOrganismsByType().get(getClass().getSimpleName());
            int maxCount = getLimit().getMaxCountInArea();
            int size = organisms.size();
            if (size >= maxCount) return;
            organisms.add(this);
        }
    }

    private boolean deleteFrom(Area area) {
        synchronized (area) {
            Set<Organism> organisms = getArea().getOrganismsByType().get(getClass().getSimpleName());
            return organisms.remove(this);
        }
    }

    @Override
    protected boolean canReproduce() {
        Set<Organism> organisms = getArea().getOrganismsByType().get(getClass().getSimpleName());
        int size = organisms.size();
        return super.canReproduce() && size >= 2;
    }

    @Override
    protected double getChildWeight() {
        return getWeight() / 2;
    }

    @Override
    public void reproduce() {
        super.reproduce();
        setWeight(getWeight() - getChildWeight() / 2);
    }

    @Override
    public void eatAndGrow() {
        super.eatAndGrow();
    }
}
