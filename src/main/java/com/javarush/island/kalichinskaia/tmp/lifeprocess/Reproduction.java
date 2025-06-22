package com.javarush.island.kalichinskaia.tmp.lifeprocess;

import com.javarush.island.kalichinskaia.logic.Organism;

public class Reproduction extends AbstractLifeProcess {

    @Override
    public void run() {
        execute(Organism::reproduce);
    }
}
