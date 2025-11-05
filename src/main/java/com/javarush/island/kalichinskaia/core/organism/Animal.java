package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.config.Config.Limit;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Organism {

    protected Animal(Limit limit, Map<String, Integer> foodMap, Area area) {
        super(limit, foodMap, area);
    }

    @Override
    public void move() {
        int maxCountStep = getLimit().getMaxSpeed();
        int countStep = ThreadLocalRandom.current().nextInt(maxCountStep + 1);
        Area currentArea = getArea();
        Area targetArea = getArea().getTargetArea(countStep);

        // Сейчас порядок блокировки не нужен, но если движение будет происходить многопоточно,
        // то возможен случай: животное 1 перемещается из A в B, животное 2 перемещается из B в A.
        // В этом случае без фиксированного порядка блокировок возможен deadlock.
        var lock1 = currentArea.hashCode() < targetArea.hashCode() ? currentArea : targetArea;
        var lock2 = lock1 == currentArea ? targetArea : currentArea;
        synchronized (lock1) {
            synchronized (lock2) {
                if (targetArea.addOrganism(this)) return;
                if (currentArea.removeOrganism(this)) setArea(targetArea);
                else targetArea.removeOrganism(this);
            }
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
        // todo impl
        super.eatAndGrow();
    }
}
