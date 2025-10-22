package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourDao;
import ru.kpfu.itis.sorokin.entity.TourEntity;
import ru.kpfu.itis.sorokin.exception.DataAccessException;

import java.sql.*;

public class TourDaoImpl implements TourDao {
    private static final String SQL_SAVE = "INSERT INTO tour (title, operator_id, destination, description, duration) VALUES (?, ?, ?, ?, ?)";

    @Override
    public TourEntity save(TourEntity tour, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, tour.getTitle());
            preparedStatement.setInt(2, tour.getOperatorId());
            preparedStatement.setString(3, tour.getDestination());
            preparedStatement.setString(4, tour.getDescription());
            preparedStatement.setInt(5, tour.getDuration());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save tour, no insert row");
            }

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    tour.setId(resultSet.getInt("id"));
                    return tour;
                } else {
                    throw new DataAccessException("Failed extract tour id");
                }

             }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save tour", e);
        }
    }
}
