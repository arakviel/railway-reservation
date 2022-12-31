package com.arakviel.persistance;

/**
 * Проста фабрика об'єктів Dao.
 */
public class DaoFactory {

    public static ClientDao getClientDao() {
        return ClientDao.getInstance();
    }

    public static StationDao getStationDao() {
        return StationDao.getInstance();
    }

    public static TicketDao getTicketDao() {
        return TicketDao.getInstance();
    }

    public static TrainDao getTrainDao() {
        return TrainDao.getInstance();
    }

    public static UserDao getUserDao() {
        return UserDao.getInstance();
    }

    private DaoFactory() {
    }
}
