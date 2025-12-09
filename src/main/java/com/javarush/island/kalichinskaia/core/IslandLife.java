package com.javarush.island.kalichinskaia.core;

import com.javarush.island.kalichinskaia.core.habitat.Island;
import com.javarush.island.kalichinskaia.config.Config;
import com.javarush.island.kalichinskaia.core.action.LifeProcess;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IslandLife {
    private final Config config;
    private final ScheduledExecutorService islandLifeExecutor;
    private final ExecutorService lifeProcessesExecutor;


    // todo add field (Component) initialized by Island

    private boolean isFinished = false; //todo how and when to set true???
    // (если статистику выводить на консоль, всегда !isFinished. Если использовать JavaFx, то вызывается сеттер со значением true.)

    public IslandLife() {
        this.config = Config.createConfigFromFile();
        this.islandLifeExecutor = Executors.newSingleThreadScheduledExecutor();
        this.lifeProcessesExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Island island = new Island(config);
        for (var lifeProcess : LifeProcess.values()) {
            lifeProcess.getAction().setIsland(island);
        }
    }

    public void start() {
        islandLifeExecutor.scheduleAtFixedRate(
                IslandLife.this::doOneStep, 0, config.getPeriod(), TimeUnit.MILLISECONDS
        );
    }

    private void doOneStep() {
        if (!isFinished) {
            for (LifeProcess lifeProcess : LifeProcess.values()) {
                lifeProcessesExecutor.submit(lifeProcess.getAction());
            }
            // todo 1.wait for finish all actions in lifeProcessesExecutor
            //      2.run view (invoke method of added component)
            return;
        }
        lifeProcessesExecutor.shutdown();
        islandLifeExecutor.shutdown();
    }
}
