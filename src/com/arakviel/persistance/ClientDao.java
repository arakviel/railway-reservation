package com.arakviel.persistance;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Set;
import com.arakviel.model.impl.Client;

/**
 * Робота із даними клієнтів.
 */
public final class ClientDao extends Dao<Client> {

    @Override
    protected Path getPath() {
        return PathFactory.CLIENT.getPath();
    }

    @Override
    protected Type getCollectionType() {
        return TypeToken.getParameterized(Set.class, Client.class).getType();
    }

    private ClientDao() {
    }

    private static class ClientDaoHolder {

        public static final ClientDao INSTANCE = new ClientDao();
    }

    static ClientDao getInstance() {
        return ClientDaoHolder.INSTANCE;
    }
}
