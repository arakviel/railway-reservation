package com.arakviel.cli.item;

import com.arakviel.cli.util.AnsiColor;
import com.arakviel.cli.view.WorkSpaceView;

public enum WorkSpaceMenuItem {
    ADD("Створити", AnsiColor.GREEN_BOLD),
    VIEW("Список", AnsiColor.CYAN_BOLD),
    EDIT("Редагувати", AnsiColor.PURPLE_BOLD),
    DELETE("Видалити", AnsiColor.RED_BOLD);

    private final String name;
    private final AnsiColor color;

    WorkSpaceMenuItem(String name, AnsiColor color) {
        this.name = name;
        this.color = color;
    }

    public void run() {
        WorkSpaceView.Process.run(this);
    }

    @Override
    public String toString() {
        return "%s[%d] %s%s".formatted(color, this.ordinal() + 1, AnsiColor.RESET, name);
    }
}
