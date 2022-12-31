package com.arakviel.cli.util;

public enum YesNoInput {
    YES("y"), NO("n");
    private String value;

    YesNoInput(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
