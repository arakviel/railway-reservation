package com.arakviel.cli.item;

import com.arakviel.cli.util.AnsiColor;
import com.arakviel.cli.view.AuthView;

/**
 * Пункти меню аутентифікації.
 */
public enum AuthMenuItem {
    AUTHENTICATION("Увійти", AnsiColor.GREEN_BOLD) {
        @Override
        public void run() {
            AuthView.ProcessSignIn.run();
        }
    },
    SIGNUP("Забули пароль?", AnsiColor.RED_BOLD) {
        @Override
        public void run() {
            AuthView.ProcessResetPassword.run();
        }
    };

    private String name;
    private AnsiColor color;

    AuthMenuItem(String name, AnsiColor color) {
        this.name = name;
        this.color = color;
    }

    public abstract void run();

    @Override
    public String toString() {
        return "%s[%d] %s%s".formatted(color, this.ordinal() + 1, AnsiColor.RESET, name);
    }
}
