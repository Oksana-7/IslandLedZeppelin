package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.config.Config.Limit;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

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
        synchronized (getArea()) {
            if (safeFindFood(getArea())) {
                if (getWeight() > 0) {
                    int growPercent = ThreadLocalRandom.current().nextInt(getLimit().getAdditional().get("maxGrowPercent"));
                    double grow = getWeight() * growPercent / 100.0;
                    double newWeight = Math.max(getLimit().getMaxWeight(), getWeight() + grow);
                    setWeight(newWeight);
                }
            }
        }
    }

    protected boolean safeFindFood(Area area) {
        synchronized (getArea()) {
            boolean foodFound = false;
            Set<Organism> organisms = getArea().getOrganismsByType().get(getClass().getSimpleName());
            if (organisms.contains(this)) {
                double needFood = getNeedFood();
                if (!(needFood <= 0)) {
                    Iterator<Map.Entry<String, Integer>> foodIterator = getFoodMap().entrySet().iterator();
                    while (needFood > 0 && foodIterator.hasNext()) {
                        Map.Entry<String, Integer> entry = foodIterator.next();
                        String keyFood = entry.getKey();
                        Integer probably = entry.getValue();
                        Map<String, Integer> foods = getFoodMap();
                        if (Objects.nonNull(foods) && !foods.isEmpty() && foods.containsValue(probably)) {
                            for (Iterator<Organism> organismIterator = organisms.iterator(); organismIterator.hasNext(); ) {
                                Organism o = organismIterator.next();
                                double foodWeight = o.getWeight();
                                double delta = Math.min(foodWeight, needFood);
                                setWeight(getWeight() + delta);
                                o.setWeight(foodWeight - delta);
                                if (o.getWeight() <= 0) {
                                    organismIterator.remove();
                                }
                                needFood -= delta;
                                foodFound = true;
                                if (needFood <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return foodFound;
        }
    }

    private double getNeedFood() {
        return Math.min(getLimit().getMaxFood(),
                getLimit().getMaxWeight() - getWeight());
    }
}
