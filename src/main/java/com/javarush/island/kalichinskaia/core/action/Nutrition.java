package com.javarush.island.kalichinskaia.core.action;

import com.javarush.island.kalichinskaia.core.organism.Organism;

public class Nutrition extends Action {

    @Override
    public void run() {
        execute(Organism::eatAndGrow);
    }
}
