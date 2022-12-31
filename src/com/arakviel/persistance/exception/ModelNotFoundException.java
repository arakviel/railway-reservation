package com.arakviel.persistance.exception;

public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException() {
        super("Не вдалось знайти модель по ідентифікатору!");
    }

    public ModelNotFoundException(String message) {
        super(message);
    }
}
