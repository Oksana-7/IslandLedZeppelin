package com.javarush.island.kalichinskaia.core.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LifeProcess {
    MOVING(new Moving()),
    NUTRITION(new Nutrition()),
    REPRODUCTION(new Reproduction());

    private final Action action;
}
