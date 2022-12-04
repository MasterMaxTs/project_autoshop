package ru.job4j.cars.repository;

import org.hibernate.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Шаблон Command
 */
public interface CrudRepository {

    /**
     * Открывает сессию, начинает транзакцию, выполняет абстрактную операцию.
     * @param command команда в виде лямбда выражения
     * @return результат выполнения команды при успешном завершении транзакции
     * иначе откат транзакции и показ исключения
     * @param <T> тип сущности
     */
    <T> T tx(Function<Session, T> command);

    /**
     * Запускает CRUD-операцию
     * @param command команда в виде лямбда выражения
     */
    void run(Consumer<Session> command);

    /**
     * Запускает CRUD-операцию
     * @param query текстовое значение запроса
     * @param args карта из параметров запроса
     */
    void run(String query, Map<String, Object> args);

    /**
     * Создаёт запрос в базу данных и выводит результат
     * @param query текстовое значение запроса
     * @param cl класс сущности модели данных
     * @return список сущностей из базы данных, удовлетворяющих
     * условию запроса
     * @param <T> тип сущности модели данных
     */
    <T> List<T> query(String query, Class<T> cl);

    /**
     * Создаёт запрос в базу данных и выводит результат
     * @param query текстовое значение запроса
     * @param cl класс сущности модели данных
     * @param args карта из параметров запроса
     * @return Optional.of(model), если сущность в виде модели данных
     * найдена в базе по запросу с указанными параметрами,
     * иначе Optional.empty()
     * @param <T> тип сущности модели данных
     */
    <T> Optional<T> optional(String query, Class<T> cl,
                             Map<String, Object> args);

    /**
     * Создаёт запрос в базу данных и выводит результат
     * @param query текстовое значение запроса
     * @param cl класс сущности модели данных
     * @param args карта из параметров запроса
     * @return список сущностей из базы данных, удовлетворяющих
     * условию запроса с указанными параметрами
     * @param <T> тип сущности модели данных
     */
    <T> List<T> query(String query, Class<T> cl, Map<String, Object> args);
}
