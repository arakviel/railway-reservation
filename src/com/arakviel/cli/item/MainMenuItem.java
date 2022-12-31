package com.arakviel.cli.item;

import com.arakviel.cli.util.AnsiColor;
import com.arakviel.cli.view.WorkSpaceView;

/**
 * Пункти основного меню.
 */
public enum MainMenuItem {
    TICKET("Білети", AnsiColor.GREEN_BOLD),
    CLIENT("Пасажири", AnsiColor.CYAN_BOLD),
    STATION("Станції", AnsiColor.PURPLE_BOLD),
    TRAIN("Поїзда", AnsiColor.YELLOW_BOLD),
    USER("Працівники", AnsiColor.BLUE_BOLD);

    private String name;
    private AnsiColor color;

    MainMenuItem(String name, AnsiColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public AnsiColor getColor() {
        return color;
    }

    public void run() {
        WorkSpaceView.init(this);
    }

    @Override
    public String toString() {
        return "%s[%d] %s%s".formatted(color, this.ordinal() + 1, AnsiColor.RESET, name);
    }
}
