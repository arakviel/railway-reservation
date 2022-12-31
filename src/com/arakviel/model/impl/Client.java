package com.arakviel.model.impl;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import com.arakviel.model.Model;

/**
 * Модель бізнес логіки "Клієнт", що покупає білет. Приклад створення
 * <p>
 * Приклад використання:
 * {@code Client.builder().id(UUID.randomUUID()).name("Іван").surname("Шевченко");}
 */
public class Client implements Model {

    private UUID id;
    private String name;
    private String surname;

    private Client(UUID id, String name, String surname) {
        if (Validation.assertName(name, "імені") && Validation.assertName(surname, "призвіщі")) {
            this.id = id;
            this.name = name;
            this.surname = surname;
        } else {
            this.id = UUID.randomUUID();
            this.name = "Іван";
            this.surname = "Шевченко";
        }
    }

    public static ClientBuilderId builder() {
        return id -> name -> surname -> () -> new Client(id, name, surname);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public interface ClientBuilderId {

        ClientBuilderName id(UUID uuid);
    }

    public interface ClientBuilderName {

        ClientBuilderSurname name(String name);
    }

    public interface ClientBuilderSurname {

        ClientBuilder surname(String surname);
    }

    public interface ClientBuilder {

        Client build();
    }

    public static class Validation {

        private static final Queue<String> errors = new ArrayDeque<>();

        public static Queue<String> getErrors() {
            Queue<String> newErrors = new ArrayDeque<>(errors);
            errors.clear();
            return newErrors;
        }

        private static boolean assertName(String name, String title) {
            boolean isValid = true;
            if (name.isBlank()) {
                errors.add("В %s пусто.".formatted(title));
                isValid = false;
            }
            if (name.length() > 32 || name.length() < 2) {
                errors.add(
                        "Довжина %s допустима в діапазоні від 2 до 32 символів.".formatted(title));
                isValid = false;
            }
            boolean isLatin = name.matches("\\p{IsLatin}+");
            boolean isCyrillic = name.matches("\\p{IsCyrillic}+");
            if (!isCyrillic) {
                if (!isLatin) {
                    errors.add(
                            "В %s допустимі лише латинські та кириличні букви.".formatted(title));
                    isValid = false;
                }
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
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "%s | %s | %s".formatted(id, name, surname);
    }
}
