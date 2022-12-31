package com.arakviel.cli.partial.page;

import static java.lang.System.out;

import com.arakviel.cli.item.MainMenuItem;
import com.arakviel.cli.partial.ExitPartial;
import com.arakviel.cli.partial.HeaderPartial;
import com.arakviel.cli.util.AnsiColor;

public final class MainPartial {

    public static void init() {
        HeaderPartial.init("Головне меню", AnsiColor.GREEN_BOLD);
        for (MainMenuItem item : MainMenuItem.values()) {
            out.println(item);
        }
        ExitPartial.init();
    }

    private MainPartial() {
    }
}
