package com.arakviel.model.impl;

/**
 * Модель бізнес логіки "Гроші", яка повязана із квитками.
 *
 * @param value гривні
 * @param penny копійки
 */
public record Money(int value, int penny) {

    @Override
    public String toString() {
        return "%d,%d ГРН".formatted(value, penny);
    }
}
