package com.arakviel.repository;

import com.arakviel.persistance.DaoFactory;
import com.arakviel.persistance.TrainDao;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import com.arakviel.model.impl.Train;

public final class TrainRepository {

    private static final TrainDao DAO = DaoFactory.getTrainDao();

    public static Optional<Train> get(int code) {
        return DAO.findById(getCodeCondition(code));
    }

    public static Set<Train> getAll() {
        return DAO.findAll();
    }

    public static Set<Train> findAll(Predicate<Train> condition) {
        return DAO.findAll(condition);
    }

    public static void add(Train train) {
        DAO.save(train);
    }

    public static void addAll(Set<Train> trains) {
        DAO.saveAll(trains);
    }

    public static void set(int code, Train train) {
        DAO.update(train, getCodeCondition(code));
    }

    public static void remove(Train train) {
        remove(train.getCode());
    }

    public static void remove(int code) {
        DAO.delete(getCodeCondition(code));
    }

    private static Predicate<Train> getCodeCondition(int code) {
        return t -> t.getCode() == code;
    }

    private TrainRepository() {
    }
}
