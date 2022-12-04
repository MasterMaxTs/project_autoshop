package ru.job4j.cars.exception;

/**
 * Пользовательский класс IllegalUserLoginException
 */
public class IllegalUserLoginException extends IllegalArgumentException {

    public IllegalUserLoginException(String s) {
        super(s);
    }
}
