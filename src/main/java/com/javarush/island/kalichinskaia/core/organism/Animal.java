package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.config.Config.Params;
import com.javarush.island.kalichinskaia.core.habitat.Area;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Organism {

    protected Animal(Params params, Map<String, Integer> foodMap, Area area) {
        super(params, foodMap, area);
    }

    @Override
    public void move() {
        int maxCountStep = getParams().getMaxSpeed();
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
        double slim = getChildWeight() / 2;
        changeWeight(slim);
    }

    @Override
    public void eatAndGrow() {
        synchronized (getArea()) {
            Set<Organism> organisms = getArea().getOrganismsByType().get(getClass().getSimpleName());
            if (!organisms.contains(this)) return;

            boolean eat = false;
            double needFood = getNeedFood();
            if (needFood <= 0) return;
            Iterator<Entry<String, Integer>> foodMapIterator = getFoodMap().entrySet().iterator();
            while (needFood > 0 && foodMapIterator.hasNext()) {
                Entry<String, Integer> entry = foodMapIterator.next();
                String keyFood = entry.getKey();
                Integer probably = entry.getValue();
                Set<Organism> foods = getArea().getOrganismsByType().get(keyFood);
                boolean isHuntSuccess = ThreadLocalRandom.current().nextDouble(100d) < probably;
                if (foods == null || foods.isEmpty() || !isHuntSuccess) continue;
                Iterator<Organism> foodIterator = foods.iterator();
                while (foodIterator.hasNext()) {
                    Organism food = foodIterator.next();
                    double foodWeight = food.getWeight();
                    double delta = Math.min(foodWeight, needFood);
                    changeWeight(-delta);
                    food.changeWeight(delta);
                    if (food.getWeight() <= 0) foodIterator.remove();
                    needFood -= delta;
                    eat = true;
                    if (needFood <= 0) break;
                }
            }
            if (eat) return;
            int slimPercent = ThreadLocalRandom.current().nextInt(getParams().getAdditional().get("slimPercent")) * -1;
            double slim = getWeight() * slimPercent / 100.0;
            changeWeight(slim);
        }
    }

    private double getNeedFood() {
        return Math.min(getParams().getMaxFood(),
                getParams().getMaxWeight() - getWeight());
    }
}
