package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.dto.CardTourDto;

import java.util.List;

public interface FavoriteDao {
    void addFavorite(Integer userId, Integer tourId);
    void removeFavorite(Integer userId, Integer tourId);
    boolean isFavorite(Integer userId, Integer tourId);
    List<Integer> findAllFavoritesByUserId(Integer userId);
    List<CardTourDto> findAllByUserId(Integer userId);
}
