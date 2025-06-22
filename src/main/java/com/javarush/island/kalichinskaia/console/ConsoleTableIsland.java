package com.javarush.island.kalichinskaia.console;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class ConsoleTableIsland {
    public class FieldIsland {
    }

    FieldIsland fieldIsland = new FieldIsland();

    public static void createTable() {
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule(); // рисуем верх таблицы

        FieldIsland[] fields = new FieldIsland[5];
        for (FieldIsland field : fields) {
            asciiTable.addRow(10, 5, 1.8); //TODO добавить объекты для заполнения таблицы
            asciiTable.addRule(); //рисуем низ таблицы
        }

        asciiTable.setTextAlignment(TextAlignment.CENTER);
        String render = asciiTable.render();
        System.out.println(render);
    }
}
