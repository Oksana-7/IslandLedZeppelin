package com.javarush.island.kalichinskaia.tmp.lifeprocess;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LifeProcess {
    MOVING(new Moving()),
    NUTRITION(new Nutrition()),
    REPRODUCTION(new Reproduction());

    private final AbstractLifeProcess process;
}
