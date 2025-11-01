package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourDao;
import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.Tour;
import ru.kpfu.itis.sorokin.entity.TourEntity;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TourDaoImpl implements TourDao {
    private static final String SQL_SAVE = "INSERT INTO tour (title, operator_id, destination, description, duration) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT title, operator_id, destination, description, duration FROM tour WHERE id=?";

    private static final String SQL_FIND_ALL = """
            SELECT tour.id, title, destination, duration, company_name, image_url
            FROM tour INNER JOIN operator ON tour.operator_id = operator.user_id
            INNER JOIN tour_image ON tour.id = tour_image.tour_id
            WHERE tour_image.is_main = true
            LIMIT ? OFFSET ?
            """;

    private static final String SQL_FIND_BY_APPLICATION_ID = """
            SELECT tour.id, title, operator_id, destination, description, duration 
            FROM tour INNER JOIN application_tour ON tour.id = application_tour.tour_id
            WHERE application_tour.id = ?
            """;

    private static final String SQL_FIND_ALL_BY_OPERATOR_ID = """
            SELECT tour.id, title, destination, duration, company_name, image_url
            FROM tour INNER JOIN operator ON tour.operator_id = operator.user_id
            INNER JOIN tour_image ON tour.id = tour_image.tour_id
            WHERE tour_image.is_main = true AND operator.user_id = ?
            """;

    private static final String SQL_DELETE_BY_ID = "DELETE FROM tour WHERE id = ?";

    private static final String SQL_UPDATE_BY_ID = """
            UPDATE tour
            SET title = ?, destination = ?, description = ?, duration = ?
            WHERE tour.id = ?
            """;

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

    @Override
    public Optional<TourEntity> findById(Integer id) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new TourEntity(
                                    id,
                                    resultSet.getString("title"),
                                    resultSet.getInt("operator_id"),
                                    resultSet.getString("destination"),
                                    resultSet.getString("description"),
                                    resultSet.getInt("duration")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select tourEntity by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<CardTourDto> findAll(int limit, int offset) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {

            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<CardTourDto> tours = new ArrayList<>();

                while (resultSet.next()) {
                    tours.add(new CardTourDto(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("destination"),
                            resultSet.getInt("duration"),
                            resultSet.getString("company_name"),
                            resultSet.getString("image_url")
                    ));
                }

                return tours;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed select tours", e);
        }
    }

    @Override
    public List<CardTourDto> findAllByOperatorId(Integer operatorId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_BY_OPERATOR_ID)) {

            preparedStatement.setInt(1, operatorId);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<CardTourDto> tours = new ArrayList<>();

                while (resultSet.next()) {
                    tours.add(new CardTourDto(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("destination"),
                            resultSet.getInt("duration"),
                            resultSet.getString("company_name"),
                            resultSet.getString("image_url")
                    ));
                }

                return tours;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed select tours by operator id", e);
        }
    }

    @Override
    public Optional<TourEntity> findByApplicationId(Integer applicationId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_APPLICATION_ID)) {

            preparedStatement.setInt(1, applicationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new TourEntity(
                                    resultSet.getInt("id"),
                                    resultSet.getString("title"),
                                    resultSet.getInt("operator_id"),
                                    resultSet.getString("destination"),
                                    resultSet.getString("description"),
                                    resultSet.getInt("duration")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select tourEntity by applicationId", e);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed delete by id", e);
        }
    }

    @Override
    public void updateById(Connection connection, TourEntity tour, Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BY_ID)) {

            preparedStatement.setString(1, tour.getTitle());
            preparedStatement.setString(2, tour.getDestination());
            preparedStatement.setString(3, tour.getDescription());
            preparedStatement.setInt(4, tour.getDuration());
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed update tour by id", e);
        }
    }

    @Override
    public void updateById(TourEntity tour, Integer id) {
        try (Connection connection = DataBaseConnectionUtil.getConnection()) {

            updateById(connection, tour, id);
        } catch (SQLException e) {
            throw new DataAccessException("Failed update tour by id", e);
        }
    }
}
