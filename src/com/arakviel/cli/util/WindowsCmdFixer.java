package com.arakviel.cli.util;

import java.io.IOException;

/**
 * TODO: не працює
 */
public final class WindowsCmdFixer {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase()
            .startsWith("win");

    static {
/*        if (IS_WINDOWS) {
            try {
                new ProcessBuilder("cmd", "/c", "chcp 1251").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                System.out.printf("%sНеймовірно рідкісна помилка. Неможливе продовження роботи.",
                        AnsiColor.RED);
                System.exit(7);
            }
        }*/
    }

    public static void init() {
        if (IS_WINDOWS) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                System.out.printf("%sНеймовірно рідкісна помилка. Неможливе продовження роботи.",
                        AnsiColor.RED);
                System.exit(7);
            }
        }
    }

    private WindowsCmdFixer() {
    }
}
