package com.arakviel.cli.partial.page;

import static java.lang.System.out;

import com.arakviel.cli.item.AuthMenuItem;
import com.arakviel.cli.partial.ExitPartial;
import com.arakviel.cli.partial.HeaderPartial;
import com.arakviel.cli.util.AnsiColor;

public final class AuthPartial {

    public static void init() {
        HeaderPartial.init("Вхід у систему", AnsiColor.GREEN_BOLD);
        for (AuthMenuItem item : AuthMenuItem.values()) {
            out.println(item);
        }
        ExitPartial.init();
    }

    private AuthPartial() {
    }
}
