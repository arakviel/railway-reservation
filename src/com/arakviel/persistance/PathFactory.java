package com.arakviel.persistance;

import java.nio.file.Path;

/**
 * Фабрика об'єктів Path для DAO.
 */
enum PathFactory {
    CLIENT("clients.json"),
    SEAT("seats.json"),
    STATION("stations.json"),
    TICKET("tickets.json"),
    TRAIN("trains.json"),
    USER("users.json");
    private final String fileName;

    PathFactory(String fileName) {
        this.fileName = fileName;
    }

    public Path getPath() {
        return Path.of("data", this.fileName);
    }
}
