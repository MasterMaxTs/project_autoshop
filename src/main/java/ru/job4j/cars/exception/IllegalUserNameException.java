package ru.job4j.cars.exception;

/**
 * Пользовательский класс IllegalUserNameException
 */
public class IllegalUserNameException extends IllegalArgumentException {

    public IllegalUserNameException(String s) {
        super(s);
    }
}
