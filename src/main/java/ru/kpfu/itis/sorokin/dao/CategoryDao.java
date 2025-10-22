package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();
}
