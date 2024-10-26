package ru.clevertec.service;

import java.util.List;

public interface UserService<T> {
    List<T> findAllUsers();

    T findById(int id);

    T save(T user);

    T updateById(int id, T user);

    void deleteById(int id);
}
