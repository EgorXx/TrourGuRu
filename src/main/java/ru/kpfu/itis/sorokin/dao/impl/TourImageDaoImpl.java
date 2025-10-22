package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourImageDao;
import ru.kpfu.itis.sorokin.entity.ImageTour;
import ru.kpfu.itis.sorokin.entity.ServiceTour;
import ru.kpfu.itis.sorokin.exception.DataAccessException;

import java.sql.*;
import java.util.List;

public class TourImageDaoImpl implements TourImageDao {
    private static final String SQL_SAVE = "INSERT INTO tour_image (tour_id, image_url, is_main) VALUES (?, ?, ?)";

    @Override
    public void add(ImageTour imageTour, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, imageTour.getTourId());
            preparedStatement.setString(2, imageTour.getImageUrl());
            preparedStatement.setBoolean(3, imageTour.getMain());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save tour_image, no insert row");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save tour_image", e);
        }
    }

    @Override
    public void addAll(List<ImageTour> imageTours, Connection connection) {
        for (ImageTour imageTour : imageTours) {
            add(imageTour, connection);
        }
    }
}
