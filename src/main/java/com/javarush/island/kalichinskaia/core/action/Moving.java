package com.javarush.island.kalichinskaia.core.action;

import com.javarush.island.kalichinskaia.core.organism.Organism;

public class Moving extends Action {

    @Override
    public Void call() {
        execute(Organism::move);
        return null;
    }
}
