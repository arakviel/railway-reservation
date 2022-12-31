package com.arakviel.repository;

import com.arakviel.persistance.DaoFactory;
import com.arakviel.persistance.TicketDao;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import com.arakviel.model.impl.Ticket;

public final class TicketRepository {

    private static final TicketDao DAO = DaoFactory.getTicketDao();

    public static Optional<Ticket> get(UUID id) {
        return DAO.findById(getIdCondition(id));
    }

    public static Set<Ticket> getAll() {
        return DAO.findAll();
    }

    public static Set<Ticket> findAll(Predicate<Ticket> condition) {
        return DAO.findAll(condition);
    }

    public static void add(Ticket ticket) {
        DAO.save(ticket);
    }

    public static void addAll(Set<Ticket> tickets) {
        DAO.saveAll(tickets);
    }

    public static void set(UUID id, Ticket ticket) {
        DAO.update(ticket, getIdCondition(id));
    }

    public static void remove(Ticket ticket) {
        remove(ticket.getId());
    }

    public static void remove(UUID id) {
        DAO.delete(getIdCondition(id));
    }

    private static Predicate<Ticket> getIdCondition(UUID id) {
        return t -> t.getId().equals(id);
    }

    private TicketRepository() {
    }
}
