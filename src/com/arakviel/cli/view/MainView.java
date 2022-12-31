package com.arakviel.cli.view;

import com.arakviel.cli.item.MainMenuItem;
import com.arakviel.cli.partial.UserChoicePartial;
import com.arakviel.cli.partial.page.MainPartial;
import java.util.Arrays;

public final class MainView {

    private static final int INPUT_SIZE = MainMenuItem.values().length;

    public static void init() {
        MainPartial.init();
        UserChoicePartial.initWithClose(
                INPUT_SIZE,
                id -> Arrays.stream(MainMenuItem.values())
                        .filter(i -> i.ordinal() == id)
                        .findFirst()
                        .orElseThrow()
                        .run()
        );
    }

    private MainView() {
    }
}
