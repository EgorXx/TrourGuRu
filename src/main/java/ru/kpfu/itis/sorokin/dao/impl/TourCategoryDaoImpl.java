package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourCategoryDao;
import ru.kpfu.itis.sorokin.dto.CategoryTourAddDto;
import ru.kpfu.itis.sorokin.entity.Category;
import ru.kpfu.itis.sorokin.entity.TourEntity;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TourCategoryDaoImpl implements TourCategoryDao {
    private static final String SQL_SAVE = "INSERT INTO tour_category (tour_id, category_id) VALUES (?, ?)";
    private static final String SQl_SELECT_CATEGORIES_BY_TOUR_ID =
            "SELECT category.id, category.title " +
            "FROM tour_category INNER JOIN category ON tour_category.category_id = category.id" +
                    "WHERE tour_category.tour_id=?";


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

    @Override
    public List<Category> findByTourId(Integer tourId) {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQl_SELECT_CATEGORIES_BY_TOUR_ID)) {

            preparedStatement.setInt(1, tourId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = new Category(
                            resultSet.getInt("id"),
                            resultSet.getString("title")
                    );

                    categories.add(category);
                }

                return categories;

            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select categories by tourId", e);
        }
    }
}
