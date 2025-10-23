package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourImageDao;
import ru.kpfu.itis.sorokin.entity.ImageTour;
import ru.kpfu.itis.sorokin.entity.ServiceTour;
import ru.kpfu.itis.sorokin.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourImageDaoImpl implements TourImageDao {
    private static final String SQL_SAVE = "INSERT INTO tour_image (tour_id, image_url, is_main) VALUES (?, ?, ?)";

    @Override
    public ImageTour save(ImageTour imageTour, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, imageTour.getTourId());
            preparedStatement.setString(2, imageTour.getImageUrl());
            preparedStatement.setBoolean(3, imageTour.getMain());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save tour_image, no insert row");
            }

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    imageTour.setId(resultSet.getInt("id"));
                    return imageTour;
                } else {
                    throw new DataAccessException("Failed extract tour_image id");
                }

            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save tour_image", e);
        }
    }

    @Override
    public List<ImageTour> saveAll(List<ImageTour> imageTours, Connection connection) {
        List<ImageTour> images = new ArrayList<>();

        for (ImageTour imageTour : imageTours) {
            ImageTour image = save(imageTour, connection);
            images.add(image);
        }

        return images;
    }
}
