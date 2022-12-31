package com.arakviel.cli.partial;

import static java.lang.System.out;

import com.arakviel.cli.util.AnsiColor;

public class InputPartial {

    public static void init() {
        out.printf("%s>>>%s ", AnsiColor.GREEN_BOLD, AnsiColor.RESET);
    }

    private InputPartial() {
    }
}
