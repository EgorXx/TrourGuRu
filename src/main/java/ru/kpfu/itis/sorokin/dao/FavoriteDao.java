package ru.kpfu.itis.sorokin.dao;

import java.util.List;

public interface FavoriteDao {
    void addFavorite(Integer userId, Integer tourId);
    void removeFavorite(Integer userId, Integer tourId);
    boolean isFavorite(Integer userId, Integer tourId);
    List<Integer> findAllFavoritesByUserId(Integer userId);
}
