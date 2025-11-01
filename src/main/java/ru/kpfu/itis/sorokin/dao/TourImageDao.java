package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.ImageTour;
import ru.kpfu.itis.sorokin.entity.ProgramTour;

import java.sql.Connection;
import java.util.List;

public interface TourImageDao {
    ImageTour save(ImageTour imageTour, Connection connection);

    List<ImageTour> saveAll(List<ImageTour> imageTours, Connection connection);

    List<ImageTour> findByTourId(Integer tourId);

    void deleteByTourId(Connection connection, Integer tourId);
}
