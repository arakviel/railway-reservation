package com.arakviel.cli.partial;

import static java.lang.System.out;

import com.arakviel.cli.util.AnsiColor;

public class ExitPartial {

    public static void init() {
        SeparatorPartial.init();
        out.printf("%s[0]%s Вийти%n", AnsiColor.YELLOW_BOLD, AnsiColor.RESET);
        SeparatorPartial.init();
    }

    private ExitPartial() {
    }
}
