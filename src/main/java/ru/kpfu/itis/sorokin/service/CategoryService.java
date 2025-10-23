package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
}
