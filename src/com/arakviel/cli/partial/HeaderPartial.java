package com.arakviel.cli.partial;

import static java.lang.System.out;

import com.arakviel.cli.util.AnsiColor;

public final class HeaderPartial {

    public static void init(String title, AnsiColor color) {
        SeparatorPartial.init();
        out.printf("%s%s%n", color, title);
        SeparatorPartial.init();
    }

    private HeaderPartial() {
    }
}
