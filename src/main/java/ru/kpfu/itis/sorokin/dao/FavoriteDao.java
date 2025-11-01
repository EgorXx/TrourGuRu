package ru.kpfu.itis.sorokin.dao;

public interface FavoriteDao {
    void addFavorite(Integer userId, Integer tourId);
    void removeFavorite(Integer userId, Integer tourId);
    boolean isFavorite(Integer userId, Integer tourId);
}
