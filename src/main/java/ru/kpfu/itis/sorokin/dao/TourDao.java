package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.TourEntity;

import java.sql.Connection;
import java.util.Optional;

public interface TourDao {
    TourEntity save(TourEntity tour, Connection connection);

    Optional<TourEntity> findById(Integer id);
}
