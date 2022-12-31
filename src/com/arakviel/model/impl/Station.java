package com.arakviel.model.impl;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import com.arakviel.model.Model;

/**
 * Модель бізнес логіки "Станція", звідки і куда будуть відправлятись маршрути поїздів.
 * <p>
 * Приклад створення об'єкту:
 * {@code Station.builder().unm(2000022).name("Київ-Пасажирський").build();}
 */
public class Station implements Model {

    /**
     * Єдина мережева розмітка (ЄМР) — система цифрового кодування залізничних станцій на території
     * країн СНД та Балтії.
     * <p>
     * Джерело:
     *
     * @see <a
     * href="https://uk.wikipedia.org/wiki/Список_залізничних_станцій_і_роз%27їздів_України_(А)">
     * Вікіпедія</a>
     */
    private int unm;
    private String name;

    public Station(int unm, String name) {
        if (Station.Validation.assertCode(unm) && Station.Validation.assertName(name)) {
            this.unm = unm;
            this.name = name;
        } else {
            this.unm = 111111;
            this.name = "";
        }
    }

    public static StationBuilderUnm builder() {
        return unm -> name -> () -> new Station(unm, name);
    }

    public int getUnm() {
        return unm;
    }

    public void setUnm(int unm) {
        this.unm = unm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface StationBuilderUnm {

        StationBuilderName unm(int unm);
    }

    public interface StationBuilderName {

        StationBuilder name(String name);
    }

    public interface StationBuilder {

        Station build();
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
                errors.add("В коді ЄМР пусто.");
                isValid = false;
            }
            long digitsCount = Integer.toString(code).chars().count();
            if (digitsCount != 6) {
                errors.add("Довжина коду ЄМР повинна відповідати 6 цифрам.");
                isValid = false;
            }
            return isValid;
        }

        private static boolean assertName(String name) {
            boolean isValid = true;
            if (name.isBlank()) {
                errors.add("В назві станції пусто.");
                isValid = false;
            }
            if (name.length() > 64 || name.length() < 2) {
                errors.add("Довжина назви станції допустима в діапазоні від 2 до 64 символів.");
                isValid = false;
            }

            boolean isCyrillic = name.matches("\\p{IsCyrillic}+");
            if (!isCyrillic) {
                errors.add("В назві станції допустимі лише кириличні букви.");
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
        Station station = (Station) o;
        return unm == station.unm;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unm);
    }

    @Override
    public String toString() {
        return "%s | %s".formatted(unm, name);
    }
}
