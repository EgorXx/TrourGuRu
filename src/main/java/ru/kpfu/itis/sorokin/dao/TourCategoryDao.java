package ru.kpfu.itis.sorokin.dao;

import java.sql.Connection;
import java.util.List;

public interface TourCategoryDao {
     void add(Integer tourId, Integer categoryId, Connection connection);

     void addAll(Integer tourId, List<Integer> categoryIds, Connection connection);
}
