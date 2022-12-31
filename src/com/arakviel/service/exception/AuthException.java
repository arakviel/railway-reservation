package com.arakviel.service.exception;

import com.arakviel.cli.util.AnsiColor;

public class AuthException extends RuntimeException {

    public AuthException() {
        super();
    }

    public AuthException(String message) {
        System.out.printf("%sНе вдалось увійти у систему \uD83D\uDE22%n", AnsiColor.RED);
    }
}
