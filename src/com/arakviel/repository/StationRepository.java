package com.arakviel.repository;

import com.arakviel.persistance.DaoFactory;
import com.arakviel.persistance.StationDao;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import com.arakviel.model.impl.Station;

public final class StationRepository {

    private static final StationDao DAO = DaoFactory.getStationDao();

    public static Optional<Station> get(int unm) {
        return DAO.findById(getUnmCondition(unm));
    }

    public static Set<Station> getAll() {
        return DAO.findAll();
    }

    public static Set<Station> findAll(Predicate<Station> condition) {
        return DAO.findAll(condition);
    }

    public static void add(Station station) {
        DAO.save(station);
    }

    public static void addAll(Set<Station> tickets) {
        DAO.saveAll(tickets);
    }

    public static void set(int unm, Station station) {
        DAO.update(station, getUnmCondition(unm));
    }

    public static void remove(Station station) {
        remove(station.getUnm());
    }

    public static void remove(int unm) {
        DAO.delete(getUnmCondition(unm));
    }

    private static Predicate<Station> getUnmCondition(int unm) {
        return s -> s.getUnm() == unm;
    }

    private StationRepository() {
    }
}
