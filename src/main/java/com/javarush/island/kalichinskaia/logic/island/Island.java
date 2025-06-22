package com.javarush.island.kalichinskaia.logic.island;

import com.javarush.island.kalichinskaia.tmp.config.Config;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Island {
    @Getter
    private Area[][] areas;

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
    }


}
