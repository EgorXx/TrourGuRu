package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.CategoryDao;
import ru.kpfu.itis.sorokin.entity.Category;
import ru.kpfu.itis.sorokin.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }
}
