package ru.job4j.cars.exception;

public class IllegalUserLoginException extends IllegalArgumentException {

    public IllegalUserLoginException(String s) {
        super(s);
    }
}
