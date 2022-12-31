package com.arakviel.persistance;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Set;
import com.arakviel.model.impl.Station;

/**
 * Робота із даними станцій.
 */
public final class StationDao extends Dao<Station> {

    @Override
    protected Path getPath() {
        return PathFactory.STATION.getPath();
    }

    @Override
    protected Type getCollectionType() {
        return TypeToken.getParameterized(Set.class, Station.class).getType();
    }

    private StationDao() {
    }

    private static class StationDaoHolder {

        public static final StationDao INSTANCE = new StationDao();
    }

    static StationDao getInstance() {
        return StationDaoHolder.INSTANCE;
    }

}
