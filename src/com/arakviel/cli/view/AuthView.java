package com.arakviel.cli.view;

import static java.lang.System.out;
import static com.arakviel.cli.util.ScannerObjectHolder.SCANNER;

import com.arakviel.cli.item.AuthMenuItem;
import com.arakviel.cli.partial.SeparatorPartial;
import com.arakviel.cli.partial.TryAgainPartial;
import com.arakviel.cli.partial.UserChoicePartial;
import com.arakviel.cli.partial.page.AuthPartial;
import com.arakviel.service.AuthService;
import com.arakviel.service.AuthService.Validation;
import java.io.Console;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import com.arakviel.cli.util.AnsiColor;

/**
 * CLI для аутентифікації.
 */
public final class AuthView {

    private static final int INPUT_SIZE = AuthMenuItem.values().length;

    public static void init() {
        AuthPartial.init();
        UserChoicePartial.initWithClose(
                INPUT_SIZE,
                id -> Arrays.stream(AuthMenuItem.values())
                        .filter(i -> i.ordinal() == id)
                        .findFirst()
                        .orElseThrow()
                        .run()
        );
    }

    /**
     * Процес аутентифікації...
     */
    public static class ProcessSignIn {

        public static void run() {
            SeparatorPartial.init();

            String login = initLoginInput();
            String password = initPasswordInput();

            if (isSignIn(login, password)) {
                initSuccess();
                MainView.init();
            } else {
                TryAgainPartial.init(ProcessSignIn::run);
            }
        }

        private static boolean isSignIn(String login, String password) {
            boolean isSignIn = AuthService.signIn(login, password);
            if (!isSignIn) {
                SeparatorPartial.init();
                out.printf("%sНе вдалось авторизуватись. Слідуйте підсказкам:%n", AnsiColor.RED);
                AuthService.Validation.getErrors().forEach(e -> out.printf("- %s%n", e));
            }
            return isSignIn;
        }

        private static void initSuccess() {
            SeparatorPartial.init();
            out.printf("""
                    %sУспішна авторизація. Програма вас запам'ятала.
                    %sПеренаправлення на робоче пространство...
                    """, AnsiColor.GREEN_UNDERLINED, AnsiColor.RESET);
            sleep();
        }


        private static String initPasswordInput() {
            return initPasswordInput("Пароль");
        }

        private static String initPasswordInput(String caption) {
            Console console = System.console();
            out.printf("%s%s: ", AnsiColor.GREEN, caption);
            String password;
            if (Objects.nonNull(console)) {
                password = new String(console.readPassword());
            } else {
                password = SCANNER.next();
            }
            return password;
        }

        private static String initLoginInput() {
            out.printf("%sЛогін: ", AnsiColor.GREEN);
            return SCANNER.next();
        }

        private static void sleep() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                out.printf("%sНеймовірно рідкісна помилка. Неможливе продовження роботи.",
                        AnsiColor.RED);
                System.exit(7);
            }
        }

        private ProcessSignIn() {
        }
    }

    /**
     * Процес скидання пароля...
     */
    public static class ProcessResetPassword {

        public static void run() {
            SeparatorPartial.init();

            out.printf("Процес скидування пароля [%sbeta%s]...%n", AnsiColor.RED_BOLD,
                    AnsiColor.RESET);

            String masterPassword = initMasterPasswordInput();
            String login = ProcessSignIn.initLoginInput();
            String password = ProcessSignIn.initPasswordInput();
            String rePassword = initRePasswordInput();

            if (isResetPassword(login, masterPassword, password, rePassword)) {
                initSuccess();
                ProcessSignIn.run();
            } else {
                TryAgainPartial.init(ProcessResetPassword::run);
            }
        }

        private static boolean isResetPassword(String login,
                String masterPassword,
                String password,
                String rePassword) {
            SeparatorPartial.init();
            AuthService.resetPassword(login, masterPassword, password, rePassword);

            Queue<String> errors = Validation.getErrors();
            boolean emptyError = errors.isEmpty();
            if (!emptyError) {
                out.printf("%sНе вдалось скинути пароль. Слідуйте підсказкам:%n", AnsiColor.RED);
                errors.forEach(e -> out.printf("- %s%n", e));
            }
            return emptyError;
        }

        private static void initSuccess() {
            out.printf("""
                    %sУспішна скидання пароля.
                    %sПеренаправлення на повторну авторизацію...
                    """, AnsiColor.GREEN_UNDERLINED, AnsiColor.RESET);
            ProcessSignIn.sleep();
        }

        private static String initRePasswordInput() {
            return ProcessSignIn.initPasswordInput("Повторіть пароль");
        }

        private static String initMasterPasswordInput() {
            return ProcessSignIn.initPasswordInput("Мастер пароль");
        }

        private ProcessResetPassword() {
        }
    }

    private AuthView() {
    }
}
