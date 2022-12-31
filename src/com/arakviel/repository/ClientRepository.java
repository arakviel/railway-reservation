package com.arakviel.repository;

import com.arakviel.persistance.ClientDao;
import com.arakviel.persistance.DaoFactory;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import com.arakviel.model.impl.Client;

public final class ClientRepository {

    private static final ClientDao DAO = DaoFactory.getClientDao();

    public static Optional<Client> get(UUID id) {
        return DAO.findById(getIdCondition(id));
    }

    public static Set<Client> getAll() {
        return DAO.findAll();
    }

    public static Set<Client> findAll(Predicate<Client> condition) {
        return DAO.findAll(condition);
    }

    public static void add(Client client) {
        DAO.save(client);
    }

    public static void addAll(Set<Client> clients) {
        DAO.saveAll(clients);
    }

    public static void set(UUID id, Client client) {
        DAO.update(client, getIdCondition(id));
    }

    public static void remove(Client client) {
        remove(client.getId());
    }

    public static void remove(UUID id) {
        DAO.delete(getIdCondition(id));
    }

    private static Predicate<Client> getIdCondition(UUID id) {
        return t -> t.getId().equals(id);
    }

    private ClientRepository() {
    }
}
