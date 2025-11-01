package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.FavoriteDao;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FavoriteDaoImpl implements FavoriteDao {
    private static final String SQL_SAVE = "INSERT INTO favorite (user_id, tour_id) VALUES (?, ?)";

    private static final String SQL_DELETE = "DELETE FROM favorite WHERE user_id = ? AND tour_id = ?";

    private static final String SQL_EXISTS = "SELECT EXISTS(SELECT 1 FROM favorite WHERE user_id = ? AND tour_id = ?)";

    @Override
    public void addFavorite(Integer userId, Integer tourId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, tourId);

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save favorite", e);
        }
    }

    @Override
    public void removeFavorite(Integer userId, Integer tourId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, tourId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed delete favorite by userId and tourId", e);
        }
    }

    @Override
    public boolean isFavorite(Integer userId, Integer tourId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_EXISTS)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, tourId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();

                return resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed check exists favorite", e);
        }
    }
}
