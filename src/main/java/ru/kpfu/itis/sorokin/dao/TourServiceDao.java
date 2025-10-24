package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.Category;
import ru.kpfu.itis.sorokin.entity.ProgramTour;
import ru.kpfu.itis.sorokin.entity.ServiceTour;

import java.sql.Connection;
import java.util.List;

public interface TourServiceDao {
    ServiceTour save(ServiceTour serviceTour, Connection connection);

    List<ServiceTour> saveAll(List<ServiceTour> serviceTours, Connection connection);

    List<ServiceTour> findByTourId(Integer tourId);
}
