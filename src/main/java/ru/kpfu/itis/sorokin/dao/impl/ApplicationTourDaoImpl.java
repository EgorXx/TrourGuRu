package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.ApplicationTourDao;
import ru.kpfu.itis.sorokin.entity.ApplicationTour;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.DuplicateApplicationException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;

public class ApplicationTourDaoImpl implements ApplicationTourDao {
    private static final String SQL_SAVE = "INSERT INTO application_tour (tour_id, user_id, status) VALUES (?, ?, ?::general_status)";

    @Override
    public void save(ApplicationTour applicationTour) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {

            preparedStatement.setInt(1, applicationTour.getTourId());
            preparedStatement.setInt(2, applicationTour.getUserId());
            preparedStatement.setString(3, applicationTour.getStatus().name());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateApplicationException("application_tour already exist");
            }

            throw new DataAccessException("Failed save application_tour", e);
        }
    }
}
