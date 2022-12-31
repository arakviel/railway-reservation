package com.arakviel.cli.partial;

import static java.lang.System.out;

import com.arakviel.cli.util.AnsiColor;

public final class LogoPartial {

    public static void init() {
        out.printf("""
                        %s           _   _   _  __    ___     ___     ___    _  _     ___  \s
                        %s    o O O | | | | | |/ /   | _ \\   /   \\   |_ _|  | \\| |   | __| \s
                        %s   o      | |_| | | ' <    |   /   | - |    | |   | .` |   | _|  \s
                        %s  TS__[O]  \\___/  |_|\\_\\   |_|_\\   |_|_|   |___|  |_|\\_|   |___| \s
                        %s {======|_|""\"""|_|""\"""|_|""\"""|_|""\"""|_|""\"""|_|""\"""|_|""\"""|\s
                        %s./o--000'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'\s
                        """, AnsiColor.BLUE, AnsiColor.BLUE, AnsiColor.BLUE, AnsiColor.YELLOW,
                AnsiColor.YELLOW,
                AnsiColor.YELLOW);
    }

    private LogoPartial() {
    }
}
