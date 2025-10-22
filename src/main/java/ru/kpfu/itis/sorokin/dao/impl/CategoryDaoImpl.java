package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.CategoryDao;
import ru.kpfu.itis.sorokin.entity.Category;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private static final String SQL_SELECT_ALL = "SELECT id, title FROM category";

    @Override
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();

        try (Connection connection = DataBaseConnectionUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setTitle(resultSet.getString("title"));
                list.add(category);
            }

            return list;
        } catch (SQLException e) {
            throw new DataAccessException("Failed select categories", e);
        }

    }
}
