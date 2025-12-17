package com.javarush.island.kalichinskaia.core;

import com.javarush.island.kalichinskaia.config.Config;
import com.javarush.island.kalichinskaia.core.action.Action;
import com.javarush.island.kalichinskaia.core.action.LifeProcess;
import com.javarush.island.kalichinskaia.core.habitat.Island;
import com.javarush.island.kalichinskaia.view.ConsoleTableIsland;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class IslandLife {
    private final Config config;
    private final ScheduledExecutorService islandLifeExecutor;
    private final ExecutorService lifeProcessesExecutor;


    // todo add field (Component) initialized by Island


    private boolean isFinished = false; //todo how and when to set true???

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
            List<Callable<Void>> tasks = new ArrayList<>();
            for (LifeProcess lifeProcess : LifeProcess.values()) {
                Action task = lifeProcess.getAction();
                tasks.add(task);
            }
            try {
                lifeProcessesExecutor.invokeAll(tasks);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Island island = new Island(config);
            ConsoleTableIsland consoleTableIsland = new ConsoleTableIsland(island);
            consoleTableIsland.showMap();
            return;
        }
        lifeProcessesExecutor.shutdown();
        islandLifeExecutor.shutdown();
    }
}
