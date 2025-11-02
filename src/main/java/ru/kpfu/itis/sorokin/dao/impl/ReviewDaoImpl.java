package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.ReviewDao;
import ru.kpfu.itis.sorokin.dto.ReviewDto;
import ru.kpfu.itis.sorokin.entity.Review;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDaoImpl implements ReviewDao {
    private static final String SQL_SAVE = "INSERT INTO review (text, user_id, tour_id) VALUES(?, ?, ?) RETURNING id, created_time";

    private static final String SQL_FIND_ALL_BY_TOUR_ID_WITH_USER = """
    SELECT review.id, review.text, review.user_id, review.tour_id, review.created_time,
           users.role,
           CASE 
               WHEN users.role = 'OPERATOR' THEN operator.company_name
               ELSE users.name
           END as name
    FROM review
    JOIN users ON review.user_id = users.id
    LEFT JOIN operator ON users.id = operator.user_id AND users.role = 'OPERATOR'
    WHERE review.tour_id = ?
    ORDER BY review.created_time DESC
    """;

    private static final String SQL_FIND_BY_ID = """
    SELECT id, text, user_id, tour_id, created_time 
    FROM review WHERE id = ?
    """;

    private static final String SQL_DELETE = "DELETE FROM review WHERE id = ?";

    @Override
    public Review save(Review review) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {

            preparedStatement.setString(1, review.getText());
            preparedStatement.setInt(2, review.getUserId());
            preparedStatement.setInt(3, review.getTourId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                review.setId(resultSet.getInt("id"));
                review.setCreatedTime(resultSet.getTimestamp("created_time").toLocalDateTime());
            }

            return review;

        } catch (SQLException e) {
            throw new DataAccessException("Failed save review", e);
        }
    }

    @Override
    public List<ReviewDto> findAllByTourId(Integer tourId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_BY_TOUR_ID_WITH_USER)) {

            preparedStatement.setInt(1, tourId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<ReviewDto> reviews = new ArrayList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                while(resultSet.next()) {
                    String createdDate = resultSet.getTimestamp("created_time").toLocalDateTime().format(formatter);

                    reviews.add(new ReviewDto(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("name"),
                            Role.valueOf(resultSet.getString("role")),
                            null,
                            resultSet.getString("text"),
                            createdDate
                    ));
                }

                return reviews;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select reviews by tour id", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE)) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed delete review by id", e);
        }
    }

    @Override
    public Optional<Review> findById(Integer id) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new Review(
                                    resultSet.getInt("id"),
                                    resultSet.getString("text"),
                                    resultSet.getInt("user_id"),
                                    resultSet.getInt("tour_id"),
                                    resultSet.getTimestamp("created_time").toLocalDateTime()
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select review by id", e);
        }

        return Optional.empty();
    }
}
