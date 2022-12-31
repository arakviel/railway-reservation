package com.arakviel.model.impl;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import com.arakviel.model.Model;

/**
 * Модель бізнес логіки "Поїзд".
 * <p>
 * Поле {@link Train#addon} є опціональним.
 */
public class Train implements Model {

    private int code;
    private String addon;

    public Train(int code) {
        this.code = Validation.assertCode(code) ? code : 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAddon() {
        return addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }

    public static class Validation {

        private static final Queue<String> errors = new ArrayDeque<>();

        public static Queue<String> getErrors() {
            Queue<String> newErrors = new ArrayDeque<>(errors);
            errors.clear();
            return newErrors;
        }

        private static boolean assertCode(int code) {
            boolean isValid = true;
            if (code == 0) {
                errors.add("Код поїзда пустий.");
                isValid = false;
            }
            long digitsCount = Integer.toString(code).chars().count();
            if (digitsCount > 6 || digitsCount < 1) {
                errors.add("Довжина коду поїзда повинна бути до 6 цифер.");
                isValid = false;
            }
            return isValid;
        }

        private Validation() {
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Train train = (Train) o;
        return code == train.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "%s | %s".formatted(code, addon);
    }
}
