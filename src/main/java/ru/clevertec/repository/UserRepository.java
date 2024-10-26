package ru.clevertec.repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository<T> {

    List<T> findAll();

    Optional<T> findById(int id);

    T save(T userEntity);

    Optional<T> updateById(int id, T userEntity);

    int deleteById(int id);
}
