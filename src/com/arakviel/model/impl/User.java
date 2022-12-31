package com.arakviel.model.impl;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import com.arakviel.model.Model;

/**
 * Модель бізнес логіки "Користувач". Також, це робітник залізниці.
 */
public class User implements Model, Serializable {

    @Serial
    private static final long serialVersionUID = 6461614019227506486L;
    public static final String MASTER_PASSWORD = "master_password";

    private UUID id;
    private String login;
    private String password;
    private Role role;

    private User(UUID id, String login, String password, Role role) {
        if (Validation.assertLoginValid(login) && Validation.assertPasswordValid(password)) {
            this.id = id;
            this.login = login;
            this.password = password;
            this.role = role;
        } else {
            this.id = id;
            this.login = "";
            this.password = "";
            this.role = Role.WORKER;
        }
    }

    public static UserBuilderId builder() {
        return id -> login -> password -> role -> () -> new User(id, login, password, role);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public interface UserBuilderId {

        UserBuilderLogin id(UUID id);
    }

    public interface UserBuilderLogin {

        UserBuilderPassword login(String login);
    }

    public interface UserBuilderPassword {

        UserBuilderRole password(String password);
    }

    public interface UserBuilderRole {

        UserBuilder role(Role role);
    }


    public interface UserBuilder {

        User build();
    }

    public enum Role implements Serializable {
        ADMIN, WORKER
    }

    public static class Validation {

        private static final Queue<String> errors = new ArrayDeque<>();

        public static Queue<String> getErrors() {
            Queue<String> newErrors = new ArrayDeque<>(errors);
            errors.clear();
            return newErrors;
        }

        private static boolean assertLoginValid(String login) {
            boolean isValid = true;
            if (login.isBlank()) {
                errors.add("Логін пустий.");
                isValid = false;
            }
            if (login.length() > 20 || login.length() < 4) {
                errors.add(
                        "Довжина логіна не повинна перевищувати 20 і повинна бути більше 4 символів.");
                isValid = false;
            }
            if (!login.matches("\\w+")) {
                errors.add(
                        "Логін повинен містити лише латинські букви, числа та символ підкреслення.");
                isValid = false;
            }
            return isValid;
        }

        private static boolean assertPasswordValid(String password) {
            boolean isValid = true;
            if (password.isBlank()) {
                errors.add("Пароль пустий.");
                isValid = false;
            }
            if (password.length() > 20 || password.length() < 8) {
                errors.add(
                        "Довжина пароля не повинна перевищувати 20 і не повинна бути більше 8 символів.");
                isValid = false;
            }
            String upperCaseChars = "(.*[A-Z].*)";
            if (!password.matches(upperCaseChars)) {
                errors.add("Пароль повинен містити хоча б один символ верхнього регістру.");
                isValid = false;
            }
            String lowerCaseChars = "(.*[a-z].*)";
            if (!password.matches(lowerCaseChars)) {
                errors.add("Пароль повинен містити хоча б один символ нижнього регістру.");
                isValid = false;
            }
            String numbers = "(.*[0-9].*)";
            if (!password.matches(numbers)) {
                errors.add("Пароль повинен містити хоча б одну цифру.");
                isValid = false;
            }
            String specialChars = "(.*[@,#,$,%].*$)";
            if (!password.matches(specialChars)) {
                errors.add("Пароль повинен містити хоча б один спеціальний символ серед @#$%.");
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
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "%s | %s | %s | %s".formatted(id, login, password, role);
    }
}