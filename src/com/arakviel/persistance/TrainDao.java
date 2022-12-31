package com.arakviel.persistance;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Set;
import com.arakviel.model.impl.Train;

/**
 * Робота із даними потягів
 */
public final class TrainDao extends Dao<Train> {

    @Override
    protected Path getPath() {
        return PathFactory.TRAIN.getPath();
    }

    @Override
    protected Type getCollectionType() {
        return TypeToken.getParameterized(Set.class, Train.class).getType();
    }

    private TrainDao() {
    }

    private static class TrainDaoHolder {

        public static final TrainDao INSTANCE = new TrainDao();
    }

    static TrainDao getInstance() {
        return TrainDaoHolder.INSTANCE;
    }

}
