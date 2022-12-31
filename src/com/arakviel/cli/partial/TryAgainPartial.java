package com.arakviel.cli.partial;

import static java.lang.System.out;
import static com.arakviel.cli.util.ScannerObjectHolder.SCANNER;

import com.arakviel.cli.util.AnsiColor;
import com.arakviel.cli.util.YesNoInput;

public final class TryAgainPartial {

    /**
     * Можливість вибору продовження. По стандарту N - вихід із додатку.
     *
     * @param yesRunnable - варіант дій при Y.
     */
    public static void init(Runnable yesRunnable) {
        init(yesRunnable, () -> System.exit(0));
    }

    /**
     * Можливість вибору продовження.
     *
     * @param yesRunnable - варіант дій при Y.
     * @param noRunnable  - варіант дій при N.
     */
    public static void init(Runnable yesRunnable, Runnable noRunnable) {
        out.printf("%sБажаєте повторити спробу? (Y/N): ", AnsiColor.YELLOW);
        String input = SCANNER.next().toLowerCase();
        if (input.equals(YesNoInput.YES.toString())) {
            yesRunnable.run();
        } else if (input.equals(YesNoInput.NO.toString())) {
            noRunnable.run();
        }
    }

    private TryAgainPartial() {
    }
}
