package com.arakviel.model.impl;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

/**
 * Модель бізнес логіки "Місце сидіння" пасажира (клієнта {@link Client}) поїзду.
 */
public class Seat {

    private int number;
    private Type type;

    public Seat(int number, Type type) {
        if (Validation.assertNumber(number)) {
            this.number = number;
            this.type = type;
        } else {
            this.number = 0;
            this.type = Type.FULL;
        }
    }

    public static SeatBuilderNumber builder() {
        return number -> type -> () -> new Seat(number, type);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public interface SeatBuilderNumber {

        SeatBuilderType number(int number);
    }

    public interface SeatBuilderType {

        SeatBuilder type(Type type);
    }

    public interface SeatBuilder {

        Seat build();
    }

    public enum Type {
        CHILD("Дитячий"),
        PREFERENTIAL("Пільговий"),
        FULL("Повний"),
        STUDENT("Студентський"),
        PENSION("Пенсійний");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Validation {

        private static final Queue<String> errors = new ArrayDeque<>();

        public static Queue<String> getErrors() {
            Queue<String> newErrors = new ArrayDeque<>(errors);
            errors.clear();
            return newErrors;
        }

        private static boolean assertNumber(int number) {
            boolean isValid = true;
            if (number == 0) {
                errors.add("В номері місця пусто.");
                isValid = false;
            }
            long digitsCount = Integer.toString(number).chars().count();
            if (digitsCount > 3 || digitsCount < 1) {
                errors.add("Довжина номера місця повинна бути до 3 цифер.");
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
        Seat seat = (Seat) o;
        return Objects.equals(number, seat.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "%s %s".formatted(number, type.getName());
    }
}
