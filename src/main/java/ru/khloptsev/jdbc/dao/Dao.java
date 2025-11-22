package ru.khloptsev.jdbc.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    E create(E e);
    boolean delete(K id);
    boolean update(E e);
    Optional<E> findById(K id);
    List<E> findAll();
}
