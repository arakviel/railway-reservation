package com.arakviel.persistance;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Set;
import com.arakviel.model.impl.User;

/**
 * Робота із даними коритувача.
 * <p>
 * Створити об'єкт можна лише через {@link DaoFactory#getUserDao()}.
 */
public final class UserDao extends Dao<User> {

    @Override
    protected Path getPath() {
        return PathFactory.USER.getPath();
    }

    @Override
    protected Type getCollectionType() {
        return TypeToken.getParameterized(Set.class, User.class).getType();
    }

    private UserDao() {
    }

    private static class UserDaoHolder {

        public static final UserDao INSTANCE = new UserDao();
    }

    static UserDao getInstance() {
        return UserDaoHolder.INSTANCE;
    }
}
