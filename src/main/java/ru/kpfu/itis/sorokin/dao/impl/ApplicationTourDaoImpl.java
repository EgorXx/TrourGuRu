package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.ApplicationTourDao;
import ru.kpfu.itis.sorokin.entity.ApplicationTour;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.Status;
import ru.kpfu.itis.sorokin.entity.TourEntity;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.DuplicateApplicationException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.Optional;

public class ApplicationTourDaoImpl implements ApplicationTourDao {
    private static final String SQL_SAVE = "INSERT INTO application_tour (tour_id, user_id, status) VALUES (?, ?, ?::general_status)";
    private static final String SQL_FIND_BY_USERID_TOURID = """
        SELECT id, user_id, tour_id, status 
        FROM application_tour 
        WHERE user_id = ? AND tour_id = ? AND status IN ('PENDING', 'APPROVED')
        LIMIT 1""";


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

    @Override
    public Optional<ApplicationTour> findByUserIdAndTourId(Integer userId, Integer tourId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_USERID_TOURID)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, tourId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new ApplicationTour(
                                    resultSet.getInt("id"),
                                    userId,
                                    tourId,
                                    Status.valueOf(resultSet.getString("status"))
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select application_tour by userId, tourId", e);
        }

        return Optional.empty();
    }
}
