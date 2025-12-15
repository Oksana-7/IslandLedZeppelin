package com.javarush.island.kalichinskaia.core.action;

import com.javarush.island.kalichinskaia.core.organism.Organism;

public class Reproduction extends Action {

    @Override
    public Void call() {
        execute(Organism::reproduce);
        return null;
    }
}
