package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.exception.ValidationException;

import java.util.List;

public interface FavoriteService {
    boolean toggleFavorite(Integer userId, Integer tourId) throws ValidationException;
    List<Integer> getAllFavoritesByUserId(Integer userId);
    List<CardTourDto> getFavoriteToursByUserId(Integer userId);
}
