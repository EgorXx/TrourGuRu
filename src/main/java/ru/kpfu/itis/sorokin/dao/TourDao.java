package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.TourEntity;

import java.sql.Connection;

public interface TourDao {
    TourEntity save(TourEntity tour, Connection connection);
}
