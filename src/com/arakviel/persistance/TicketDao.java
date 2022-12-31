package com.arakviel.persistance;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Set;
import com.arakviel.model.impl.Ticket;

/**
 * Робота із даними квитків.
 */
public final class TicketDao extends Dao<Ticket> {

    @Override
    protected Path getPath() {
        return PathFactory.TICKET.getPath();
    }

    @Override
    protected Type getCollectionType() {
        return TypeToken.getParameterized(Set.class, Ticket.class).getType();
    }

    private static class TicketDaoHolder {

        public static final TicketDao INSTANCE = new TicketDao();
    }

    static TicketDao getInstance() {
        return TicketDaoHolder.INSTANCE;
    }
}
