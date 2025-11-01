package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.entity.TourEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface TourDao {
    TourEntity save(TourEntity tour, Connection connection);

    Optional<TourEntity> findById(Integer id);

    List<CardTourDto> findAll(int limit, int offset);

    List<CardTourDto> findAllByOperatorId(Integer operatorId);

    Optional<TourEntity> findByApplicationId(Integer applicationId);

    void deleteById(Integer id);

    void updateById(Connection connection, TourEntity tour, Integer id);

    void updateById(TourEntity tour, Integer id);
}
