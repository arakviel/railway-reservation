package com.arakviel.cli.partial;

import static java.lang.System.out;

import com.arakviel.cli.util.AnsiColor;

public final class ReturnPartial {

    public static void init() {
        SeparatorPartial.init();
        out.printf("%s[0] Повернутись%s%n", AnsiColor.YELLOW_BOLD, AnsiColor.RESET);
        SeparatorPartial.init();
    }

    private ReturnPartial() {
    }
}
