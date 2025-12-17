package com.javarush.island.kalichinskaia.core.habitat;

import com.javarush.island.kalichinskaia.config.Config;
import com.javarush.island.kalichinskaia.core.organism.Organism;
import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

public class Island {
    @Getter
    private final Area[][] areas;

    @Getter
    private final Map<Organism, Long> statistics;

    public Area getAreas(int row, int col) {
        return areas[row][col];
    }

    public Island(Config config) {
        this.areas = new Area[config.getRows()][config.getCols()];
        for (int i = 0; i < areas.length; i++) {
            for (int j = 0; j < areas[i].length; j++) {
                areas[i][j] = new Area(config);
            }
        }

        for (int i = 0; i < areas.length; i++) {
            for (int j = 0; j < areas[i].length; j++) {
                List<Area> neighborAreas = new ArrayList<>();
                if (i > 0) neighborAreas.add(areas[i - 1][j]);
                if (i < areas.length - 1) neighborAreas.add(areas[i + 1][j]);
                if (j > 0) neighborAreas.add(areas[i][j - 1]);
                if (j < areas[i].length - 1) neighborAreas.add(areas[i][j + 1]);
                areas[i][j].setNeighborAreas(neighborAreas);
            }
        }
        statistics = new LinkedHashMap<>();
    }

}
