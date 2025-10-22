package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourCategoryDao;
import ru.kpfu.itis.sorokin.exception.DataAccessException;

import java.sql.*;
import java.util.List;

public class TourCategoryDaoImpl implements TourCategoryDao {
    private static final String SQL_SAVE = "INSERT INTO tour_category (tour_id, category_id) VALUES (?, ?)";


    @Override
    public void add(Integer tourId, Integer categoryId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {

            preparedStatement.setInt(1, tourId);
            preparedStatement.setInt(2, categoryId);

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save tour_category, no insert row");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save tour_category", e);
        }
    }

    @Override
    public void addAll(Integer tourId, List<Integer> categoryIds, Connection connection) {
        for (Integer categoryId : categoryIds) {
            add(tourId, categoryId, connection);
        }
    }
}
