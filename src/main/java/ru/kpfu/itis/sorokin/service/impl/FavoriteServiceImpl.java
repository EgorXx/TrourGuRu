package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.ApplicationTourDao;
import ru.kpfu.itis.sorokin.dao.FavoriteDao;
import ru.kpfu.itis.sorokin.dao.TourDao;
import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.FavoriteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FavoriteServiceImpl implements FavoriteService {
    private TourDao tourDao;
    private FavoriteDao favoriteDao;

    public FavoriteServiceImpl(TourDao tourDao, FavoriteDao favoriteDao) {
        this.tourDao = tourDao;
        this.favoriteDao = favoriteDao;
    }

    @Override
    public boolean toggleFavorite(Integer userId, Integer tourId) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (!tourDao.findById(tourId).isPresent()) {
            errors.put("tour", "тур не найден");
            throw new ValidationException(errors);
        }

        try {
            if (favoriteDao.isFavorite(userId, tourId)) {
                favoriteDao.removeFavorite(userId, tourId);
                return false;
            } else {
                favoriteDao.addFavorite(userId, tourId);
                return true;
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Failed toggle favorite", e);
        }

    }

    @Override
    public List<Integer> getAllFavoritesByUserId(Integer userId) {
        try {
            return favoriteDao.findAllFavoritesByUserId(userId);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed get favorites by userId", e);
        }
    }

    @Override
    public List<CardTourDto> getFavoriteToursByUserId(Integer userId) {
        try {
            return favoriteDao.findAllToursByUserId(userId);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed get favorite tours", e);
        }
    }
}
