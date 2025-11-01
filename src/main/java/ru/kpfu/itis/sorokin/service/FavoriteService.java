package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.exception.ValidationException;

public interface FavoriteService {
    boolean toggleFavorite(Integer userId, Integer tourId) throws ValidationException;
}
