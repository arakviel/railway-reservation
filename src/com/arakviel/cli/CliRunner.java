package com.arakviel.cli;

import static java.lang.System.out;

import java.util.Objects;
import com.arakviel.cli.partial.LogoPartial;
import com.arakviel.cli.util.AnsiColor;
import com.arakviel.cli.util.WindowsCmdFixer;
import com.arakviel.cli.view.AuthView;
import com.arakviel.cli.view.MainView;
import com.arakviel.model.impl.User;
import com.arakviel.service.AuthService;

public final class CliRunner {

    public static void init() {
        WindowsCmdFixer.init();
        LogoPartial.init();
        // TODO: refactor.
        User user = AuthService.recallUser();
        if (Objects.nonNull(user)) {
            out.printf("%sАвторизовано як: %s%s%s%n", AnsiColor.PURPLE, AnsiColor.PURPLE_UNDERLINED,
                    user.getLogin(), AnsiColor.RESET);
            MainView.init();
        } else {
            AuthView.init();
        }
    }

    private CliRunner() {
    }
}
