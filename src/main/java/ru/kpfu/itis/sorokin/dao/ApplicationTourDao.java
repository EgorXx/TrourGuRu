package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.ApplicationTour;

import java.util.Optional;


public interface ApplicationTourDao {
    void save(ApplicationTour applicationTour);

    Optional<ApplicationTour> findByUserIdAndTourId(Integer userId, Integer tourId);
}
