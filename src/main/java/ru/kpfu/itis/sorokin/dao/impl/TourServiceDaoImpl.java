package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourCategoryDao;
import ru.kpfu.itis.sorokin.dao.TourServiceDao;
import ru.kpfu.itis.sorokin.entity.ServiceTour;
import ru.kpfu.itis.sorokin.entity.TourEntity;
import ru.kpfu.itis.sorokin.exception.DataAccessException;

import java.sql.*;
import java.util.List;

public class TourServiceDaoImpl implements TourServiceDao {
    private static final String SQL_SAVE = "INSERT INTO service_toure (tour_id, title) VALUES (?, ?)";

    @Override
    public ServiceTour save(ServiceTour serviceTour, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, serviceTour.getTourId());
            preparedStatement.setString(2, serviceTour.getTitle());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save service_tour, no insert row");
            }

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    serviceTour.setId(resultSet.getInt("id"));
                    return serviceTour;
                } else {
                    throw new DataAccessException("Failed extract service_tour id");
                }

            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save service_tour", e);
        }
    }
}
