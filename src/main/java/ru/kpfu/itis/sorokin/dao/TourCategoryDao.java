package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.Category;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface TourCategoryDao {
     void add(Integer tourId, Integer categoryId, Connection connection);

     void addAll(Integer tourId, List<Integer> categoryIds, Connection connection);

     List<Category> findByTourId(Integer tourId);

     void deleteByTourId(Connection connection, Integer tourId);
}
