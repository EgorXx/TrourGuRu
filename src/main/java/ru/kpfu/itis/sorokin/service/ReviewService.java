package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.ReviewAddDto;
import ru.kpfu.itis.sorokin.dto.ReviewDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ValidationException;

import java.util.List;

public interface ReviewService {
    ReviewDto add(ReviewAddDto reviewAddDto, UserSessionDto userSessionDto) throws ValidationException;

    void deleteReview(Integer reviewId, Integer userId) throws ValidationException;

    List<ReviewDto> getReviewsByTourId(Integer tourId);
}
