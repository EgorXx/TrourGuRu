package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.ImageTour;

import java.sql.Connection;
import java.util.List;

public interface TourImageDao {
    void add(ImageTour imageTour, Connection connection);

    void addAll(List<ImageTour> imageTours, Connection connection);
}
