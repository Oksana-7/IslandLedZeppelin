package com.javarush.island.kalichinskaia.core.organism;

import com.javarush.island.kalichinskaia.config.Config.Limit;
import com.javarush.island.kalichinskaia.core.habitat.Area;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
                    setWeight(getWeight() + delta); // todo may be use universal method???
                    food.setWeight(foodWeight - delta); // todo may be use universal method???
                    if (food.getWeight() <= 0) foodIterator.remove();
                    needFood -= delta;
                    eat = true;
                    if (needFood <= 0) break;
                }
            }
            if (eat) return;
            // todo slim via universal method + settings.yml -done
            int slimPercent = ThreadLocalRandom.current().nextInt(getLimit().getAdditional().get("maxSlimPercent")) * -1; // todo use name "slimPercent"
            double slim = getWeight() * slimPercent / 100.0;
            double newWeight = Math.max(getLimit().getMaxWeight(), getWeight() - slim); // todo logic mistake
            setWeight(newWeight);

        }
    }

    private double getNeedFood() {
        return Math.min(getLimit().getMaxFood(),
                getLimit().getMaxWeight() - getWeight());
    }
}
