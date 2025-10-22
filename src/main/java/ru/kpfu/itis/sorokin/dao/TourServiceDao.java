package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.ServiceTour;

import java.sql.Connection;

public interface TourServiceDao {
    ServiceTour save(ServiceTour serviceTour, Connection connection);
}
