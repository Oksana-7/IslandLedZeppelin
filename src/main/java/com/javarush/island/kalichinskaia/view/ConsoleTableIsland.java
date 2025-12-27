package com.javarush.island.kalichinskaia.view;

import com.javarush.island.kalichinskaia.config.Config;
import com.javarush.island.kalichinskaia.core.habitat.Area;
import com.javarush.island.kalichinskaia.core.habitat.Island;

import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.javarush.island.kalichinskaia.view.Symbols.*;

public class ConsoleTableIsland {
    private int rows;
    private int cols;
    private final boolean cutRows;
    private final boolean cutCols;

    private final Island island;
    private final int cellCharCount;
    private final String border;
    private final String topBorder;
    private final String centerBorder;
    private final String bottomBorder;
    private final String bottomInfBorder;
    private final Config config;

    public ConsoleTableIsland(Island island, Config config) {
        this.island = island;
        this.config = config;
        cellCharCount = config.getConsoleCellCharCount();
        border = "═".repeat(cellCharCount);

        int showRows = config.getConsoleShowRows();
        rows = config.getRows();
        cutRows = rows > showRows;
        rows = cutRows ? showRows : rows;

        int showCols = config.getConsoleShowCols();
        cols = config.getCols();
        cutCols = cols > showCols;
        cols = cutCols ? showCols : cols;

        topBorder = border(cols, LEFT_TOP, TOP, RIGHT_TOP);
        centerBorder = border(cols, LEFT, CENTER, RIGHT);
        bottomBorder = border(cols, LEFT_BOTTOM, CENTER_BOTTOM, RIGHT_BOTTOM);
        bottomInfBorder = String.valueOf(INF_MARGIN).repeat(((cellCharCount + 1) * showCols) + 1);
    }

    public void show() {
        showMap();
        showStatistics();
        showScale();
        System.out.println();
    }

    public void showStatistics() {
        System.out.println(island.getStatistics());
    }

    public void showScale() {
        int n = 100;
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = 10; i <= n; i += 10) {
            String color = Color.getColor(i, n);
            joiner.add(color + i + "%" + Color.RESET);
        }
        System.out.println("Scale: " + joiner);
    }

    private String border(int cols, char left, char center, char right) {
        right = cutCols ? INF_MARGIN : right;
        return (IntStream.range(0, cols)
                .mapToObj(col -> (col == 0 ? left : center) + border)
                .collect(Collectors.joining(BLANK, BLANK, String.valueOf(right))));
    }

    public void showMap() {
        StringBuilder out = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            out.append(row == 0 ? topBorder : centerBorder).append(LINE_BREAK);
            for (int col = 0; col < cols; col++) {
                String residentSting = getResidentSting(island.getAreas(row, col));
                out.append(String.format(CELL_MARGIN + "%-" + cellCharCount + "s", residentSting));
            }
            out.append(cutCols ? INF_MARGIN : CELL_MARGIN).append(LINE_BREAK);
        }
        out.append(cutRows ? bottomInfBorder : bottomBorder);
        System.out.println(out);
    }

    private String getResidentSting(Area area) {
//        int maxCount = config.getOrganismParams().get(getResidentSting(area)).getMaxCountInArea();
        synchronized (area) {
            String collect = area.getOrganismsByType().entrySet().stream()
                    .filter((organismsOfType) -> !organismsOfType.getValue().isEmpty())
                    .sorted((organismsOfType1, organismsOfType2) -> organismsOfType2.getValue().size() - organismsOfType1.getValue().size())
                    .limit(cellCharCount)
                    .map(organismsOfType -> Color.getColor(organismsOfType.getValue().size(), config.getOrganismParams().get(organismsOfType.getKey()).getMaxCountInArea())
                            + organismsOfType.getKey().charAt(0)
                            + Color.RESET
                    )
                    .map(Object::toString)
                    .collect(Collectors.joining());
            long count = area
                    .getOrganismsByType()
                    .values()
                    .stream()
                    .filter((list) -> !list.isEmpty())
                    .limit(cellCharCount)
                    .count();
            String blank = count < cellCharCount ? DOT.repeat((int) (cellCharCount - count)) : BLANK;
            return collect + blank;
        }
    }
}
