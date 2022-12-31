package com.arakviel;

import static java.lang.System.out;

import com.arakviel.cli.CliRunner;
import com.arakviel.cli.util.AnsiColor;

public class Main {

    public static void main(String[] args) {
        try {
            CliRunner.init();
        } catch (Exception e) {
            out.printf("""
                    %sКритична помилка%s😢. Напевно, це невірний ввід очікуваних даних.
                    На жаль, програма не може продовжити роботу.
                    """, AnsiColor.RED, AnsiColor.RESET);
        }
    }
}
