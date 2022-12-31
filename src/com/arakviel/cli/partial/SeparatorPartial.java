package com.arakviel.cli.partial;

import static java.lang.System.out;

import com.arakviel.cli.util.AnsiColor;

public final class SeparatorPartial {

    private static final int HYPHEN_COUNT = 65;

    public static void init() {
        out.printf("%s%s%n", AnsiColor.RESET, "-".repeat(HYPHEN_COUNT));
    }

    private SeparatorPartial() {
    }
}
