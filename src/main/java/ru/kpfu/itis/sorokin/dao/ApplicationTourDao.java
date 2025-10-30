package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.dto.OperatorApplicationTourDto;
import ru.kpfu.itis.sorokin.dto.UserApplicationTourDto;
import ru.kpfu.itis.sorokin.entity.ApplicationTour;

import java.util.List;
import java.util.Optional;


public interface ApplicationTourDao {
    void save(ApplicationTour applicationTour);

    Optional<ApplicationTour> findByUserIdAndTourId(Integer userId, Integer tourId);

    List<UserApplicationTourDto> findAllByUserId(Integer userId);

    List<OperatorApplicationTourDto> findAllByOperatorId(Integer operatorId);

    void deleteById(Integer id);

    Optional<ApplicationTour> findById(Integer id);
}
