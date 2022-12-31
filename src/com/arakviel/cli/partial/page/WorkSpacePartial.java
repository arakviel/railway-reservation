package com.arakviel.cli.partial.page;

import static java.lang.System.out;

import com.arakviel.cli.item.MainMenuItem;
import com.arakviel.cli.item.WorkSpaceMenuItem;
import com.arakviel.cli.partial.HeaderPartial;
import com.arakviel.cli.partial.ReturnPartial;

public class WorkSpacePartial {

    public static void init(MainMenuItem mainMenuItem) {
        HeaderPartial.init("Робочий простір '%s'".formatted(mainMenuItem.getName()),
                mainMenuItem.getColor());
        for (WorkSpaceMenuItem item : WorkSpaceMenuItem.values()) {
            out.println(item);
        }
        ReturnPartial.init();
    }

    private WorkSpacePartial() {
    }
}
