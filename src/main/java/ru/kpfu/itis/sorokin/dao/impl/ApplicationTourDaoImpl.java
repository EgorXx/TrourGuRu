package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.ApplicationTourDao;
import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.dto.OperatorApplicationTourDto;
import ru.kpfu.itis.sorokin.dto.UserApplicationTourDto;
import ru.kpfu.itis.sorokin.entity.ApplicationTour;
import ru.kpfu.itis.sorokin.entity.Status;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.DuplicateApplicationException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplicationTourDaoImpl implements ApplicationTourDao {
    private static final String SQL_SAVE = "INSERT INTO application_tour (tour_id, user_id, status) VALUES (?, ?, ?::general_status)";

    private static final String SQL_FIND_BY_USERID_TOURID = """
        SELECT id, user_id, tour_id, status 
        FROM application_tour 
        WHERE user_id = ? AND tour_id = ? AND status IN ('PENDING', 'APPROVED')
        LIMIT 1""";

    private static final String SQL_FIND_ALL_WITH_CARDTOUR_BY_USERID = """
            SELECT application_tour.id, application_tour.user_id, application_tour.status, tour.id as tour_id, title, destination, duration, company_name, image_url
            FROM application_tour INNER JOIN tour ON application_tour.tour_id = tour.id
            INNER JOIN operator ON tour.operator_id = operator.user_id
            INNER JOIN tour_image ON tour.id = tour_image.tour_id
            WHERE tour_image.is_main = true AND application_tour.user_id = ? AND application_tour.status IN ('PENDING', 'APPROVED')
            """;

    private static final String SQL_FIND_ALL_WITH_TOURINFO_BY_OPERATORID = """
            SELECT application_tour.id, application_tour.status, users.name, users.email, tour.title, tour.destination, tour.duration
            FROM application_tour INNER JOIN users ON application_tour.user_id = users.id
            INNER JOIN tour ON application_tour.tour_id = tour.id
            WHERE tour.operator_id = ?
            """;

    private static final String DELETE_BY_ID = """
            DELETE FROM application_tour WHERE id = ?
            """;

    private static final String SQL_FIND_BY_ID = """
            SELECT id, tour_id, user_id, status FROM application_tour WHERE id = ?
            """;


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

    @Override
    public List<UserApplicationTourDto> findAllByUserId(Integer userId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_WITH_CARDTOUR_BY_USERID)) {

            preparedStatement.setInt(1, userId);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<UserApplicationTourDto> applications = new ArrayList<>();

                while (resultSet.next()) {
                    CardTourDto cardTourDto = new CardTourDto(
                            resultSet.getInt("tour_id"),
                            resultSet.getString("title"),
                            resultSet.getString("destination"),
                            resultSet.getInt("duration"),
                            resultSet.getString("company_name"),
                            resultSet.getString("image_url")
                    );

                    UserApplicationTourDto userApplicationTourDto = new UserApplicationTourDto(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            Status.valueOf(resultSet.getString("status")),
                            cardTourDto
                    );

                    applications.add(userApplicationTourDto);
                }

                return applications;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed select applications", e);
        }
    }

    @Override
    public List<OperatorApplicationTourDto> findAllByOperatorId(Integer operatorId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_WITH_TOURINFO_BY_OPERATORID)) {

            preparedStatement.setInt(1, operatorId);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<OperatorApplicationTourDto> applications = new ArrayList<>();

                while (resultSet.next()) {
                    applications.add(new OperatorApplicationTourDto(
                            resultSet.getInt("id"),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getString("email"),
                            resultSet.getString("name"),
                            resultSet.getString("title"),
                            resultSet.getString("destination"),
                            resultSet.getInt("duration")
                    ));

                }

                return applications;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed select applications", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed delete application_tour by id", e);
        }
    }

    @Override
    public Optional<ApplicationTour> findById(Integer id) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new ApplicationTour(
                                    id,
                                    resultSet.getInt("user_id"),
                                    resultSet.getInt("tour_id"),
                                    Status.valueOf(resultSet.getString("status"))
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select ApplicationTour by id", e);
        }

        return Optional.empty();
    }
}
