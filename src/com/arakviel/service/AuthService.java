package com.arakviel.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.arakviel.repository.UserRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;
import com.arakviel.model.impl.User;
import com.arakviel.service.exception.AuthException;

/**
 * Сервіс аутентифікації
 */
public final class AuthService {

    public static boolean signIn(User user) {
        return signIn(user.getLogin(), user.getPassword());
    }

    public static boolean signIn(String login, String password) {
        if (!Validation.assertLoginValid(login)
                || !Validation.assertUserFound(login)
                || !Validation.assertPasswordValid(password)) {
            return false;
        }
        User user = UserRepository.getByLogin(login).orElseThrow(AuthException::new);
        boolean verified = BCrypt.verifyer()
                .verify(password.toCharArray(), user.getPassword())
                .verified;
        if (!verified) {
            Validation.errors.add("Невірний пароль для користувача %s.".formatted(login));
        } else {
            rememberUser(user);
        }
        return verified;
    }

    public static void signUp(User user) {
        signUp(user.getLogin(), user.getPassword(), user.getRole());
    }

    public static void signUp(String login, String password, User.Role role) {
        if (!Validation.assertLoginAndPasswordPresent(login, password)
                || !Validation.assertLoginValid(login)
                || !Validation.assertPasswordValid(password)
                || !Validation.assertUserNotFound(login)) {
            return;
        }

        UserRepository.add(User.builder()
                .id(UUID.randomUUID())
                .login(login)
                .password(BCrypt.withDefaults().hashToString(12, password.toCharArray()))
                .role(role)
                .build());
    }

    public static void resetPassword(User user, String masterPassword, String rePassword) {
        resetPassword(user.getLogin(), user.getPassword(), masterPassword, rePassword);
    }

    // TODO: не вірна реалізація.
    public static void resetPassword(String login, String masterPassword, String password,
            String rePassword) {
        if (!Validation.assertMasterPassword(masterPassword)
                || !Validation.assertLoginAndPasswordPresent(login, password)
                || !Validation.assertLoginValid(login)
                || !Validation.assertPasswordValid(password)
                || !Validation.assertUserFound(login)
                || !Validation.assertRePasswordPresent(rePassword)
                || !Validation.assertPasswordAndRePasswordEquals(password, rePassword)) {
            return;
        }

        User user = UserRepository.getByLogin(login).orElseThrow(AuthException::new);
        UserRepository.setByLogin(login, User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(BCrypt.withDefaults().hashToString(12, password.toCharArray()))
                .role(user.getRole())
                .build());
    }

    // TODO: move to DAO and Repository layer
    public static User recallUser() {
        Path data = Path.of("data", "user.obj");
        if (Files.exists(data)) {
            try (var fileInputStream = new FileInputStream(data.toFile());
                    var objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (User) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    // TODO: move to DAO and Repository layer
    private static void rememberUser(User user) {
        try (var fileOutputStream = new FileOutputStream(Path.of("data", "user.obj").toFile());
                var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static class Validation {

        private static final Queue<String> errors = new ArrayDeque<>();

        public static Queue<String> getErrors() {
            Queue<String> newErrors = new ArrayDeque<>(errors);
            errors.clear();
            return newErrors;
        }

        private static boolean assertLoginAndPasswordPresent(String login, String password) {
            boolean isValid = true;
            if (login.isBlank() && password.isBlank()) {
                errors.add("Логін і/або пароль пусті.");
                isValid = false;
            }
            return isValid;
        }

        private static boolean assertUserNotFound(String login) {
            boolean isValid = true;
            if (UserRepository.getByLogin(login).isPresent()) {
                errors.add("Користувач із таким логіном вже існує.");
                isValid = false;
            }
            return isValid;
        }

        public static boolean assertUserFound(String login) {
            boolean isValid = true;
            if (UserRepository.getByLogin(login).isEmpty()) {
                errors.add("Користувача із таким логіном не знайдено.");
                isValid = false;
            }
            return isValid;
        }

        private static boolean assertLoginValid(String login) {
            boolean isValid = true;
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

        private static boolean assertRePasswordPresent(String rePassword) {
            boolean isValid = true;
            if (rePassword.isBlank()) {
                errors.add("Перевірочний пароль пустий.");
                isValid = false;
            }
            return isValid;
        }

        private static boolean assertPasswordAndRePasswordEquals(String password,
                String rePassword) {
            boolean isValid = true;
            if (!rePassword.equals(password)) {
                errors.add("Перевірочний пароль не співпадає із веденим паролем.");
                isValid = false;
            }
            return isValid;
        }

        public static boolean assertMasterPassword(String masterPassword) {
            boolean isValid = true;

            if (!User.MASTER_PASSWORD.equals(masterPassword)) {
                errors.add("Мастер пароль не співпадає із веденим паролем.");
                isValid = false;
            }
            return isValid;
        }

        private Validation() {
        }

    }

    private AuthService() {
    }
}
