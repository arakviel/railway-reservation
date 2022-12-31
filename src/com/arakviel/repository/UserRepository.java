package com.arakviel.repository;

import com.arakviel.persistance.DaoFactory;
import com.arakviel.persistance.UserDao;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import com.arakviel.model.impl.User;

public final class UserRepository {

    private static final UserDao DAO = DaoFactory.getUserDao();

    public static Optional<User> get(UUID id) {
        return DAO.findById(getIdCondition(id));
    }

    /**
     * Знайти користувача за його логіном.
     *
     * @param login логін користувача.
     * @return об'єкт моделі користувача або null.
     */
    public static Optional<User> getByLogin(String login) {
        return DAO.findById(getLoginCondition(login));
    }

    public static Set<User> getAll() {
        return DAO.findAll();
    }

    public static Set<User> findAll(Predicate<User> condition) {
        return DAO.findAll(condition);
    }

    public static void add(User user) {
        DAO.save(user);
    }

    public static void addAll(Set<User> users) {
        DAO.saveAll(users);
    }

    public static void set(UUID id, User user) {
        DAO.update(user, getIdCondition(id));
    }

    public static void setByLogin(String login, User user) {
        DAO.update(user, getLoginCondition(login));
    }

    public static void remove(User user) {
        remove(user.getId());
    }

    public static void remove(UUID id) {
        DAO.delete(getIdCondition(id));
    }

    private static Predicate<User> getIdCondition(UUID id) {
        return u -> u.getId().equals(id);
    }

    private static Predicate<User> getLoginCondition(String login) {
        return u -> u.getLogin().equals(login);
    }

    private UserRepository() {
    }
}
