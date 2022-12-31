package com.arakviel.cli.partial;

import static java.lang.System.out;
import static com.arakviel.cli.util.ScannerObjectHolder.SCANNER;

import com.arakviel.cli.util.AnsiColor;
import com.arakviel.cli.view.MainView;

public final class UserChoicePartial {

    public static void initWithClose(int inputSize, RunnableWithParam runnableWithParam) {
        init(inputSize, runnableWithParam, () -> System.exit(0));
    }

    public static void initWithReturn(int inputSize, RunnableWithParam runnableWithParam,
            Runnable zeroInputRunnable) {
        init(inputSize, runnableWithParam, zeroInputRunnable);
    }

    // TODO: недороблено. Є проблеми валідації
    private static void init(int inputSize, RunnableWithParam runnableWithParam,
            Runnable zeroInputRunnable) {
        InputPartial.init();
        int input = SCANNER.nextInt();

        // assertValidInput
        if (input > inputSize || input < 0) {
            out.printf("%sНе вірне значення. Очікувані значення: цифра від 0 до %d%n",
                    AnsiColor.RED, inputSize);
            TryAgainPartial.init(() -> init(inputSize, runnableWithParam, zeroInputRunnable),
                    MainView::init);
        } else if (input == 0) {
            zeroInputRunnable.run();
        } else {

            final int itemId = input - 1;
            runnableWithParam.run(itemId);
        }
        // /assertValidInput
    }

    @FunctionalInterface
    public interface RunnableWithParam {

        void run(int itemId);
    }
}
