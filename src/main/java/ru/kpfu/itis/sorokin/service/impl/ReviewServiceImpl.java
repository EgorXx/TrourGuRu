package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.*;
import ru.kpfu.itis.sorokin.dto.ReviewAddDto;
import ru.kpfu.itis.sorokin.dto.ReviewDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.entity.Review;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.TourEntity;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.ReviewService;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReviewServiceImpl implements ReviewService {
    private ReviewDao reviewDao;
    private TourDao tourDao;

    public ReviewServiceImpl(ReviewDao reviewDao, UserDao userDao, TourDao tourDao, OperatorDao operatorDao) {
        this.reviewDao = reviewDao;
        this.tourDao = tourDao;
    }

    @Override
    public ReviewDto add(ReviewAddDto reviewAddDto, UserSessionDto userSessionDto) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        String text = reviewAddDto.text();
        Integer tourId = reviewAddDto.tourId();

        if (text == null || text.isEmpty()) {
            errors.put("text", "текст комментария не может быть пустым");
            throw new ValidationException(errors);
        } else if (text.length() > 1023) {
            errors.put("text", "текст комментария слишком длинный, он не должен превышать 1023 символа");
            throw new ValidationException(errors);
        }

        Optional<TourEntity> tourOptional = tourDao.findById(tourId);

        if (!tourOptional.isPresent()) {
            errors.put("tour", "Тур с таким id не существует");
            throw new ValidationException(errors);
        }

        TourEntity tour = tourOptional.get();

        if (!(userSessionDto.isUser() || (userSessionDto.isOperator() && tour.getOperatorId().equals(userSessionDto.id())))) {
            errors.put("root", "У вас недостаточно прав, чтобы оставить комментарий");
            throw new ValidationException(errors);
        }

        try {
            Review review = new Review();
            review.setText(text);
            review.setUserId(userSessionDto.id());
            review.setTourId(tourId);

            Review resultReview = reviewDao.save(review);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String createdDate = resultReview.getCreatedTime().format(formatter);

            Boolean isOwner = userSessionDto.isOperator() &&
                    tour.getOperatorId().equals(userSessionDto.id());

            return new ReviewDto(
                    resultReview.getId(),
                    userSessionDto.id(),
                    userSessionDto.displayName(),
                    userSessionDto.role(),
                    isOwner,
                    resultReview.getText(),
                    createdDate
            );
        } catch (DataAccessException e) {
            throw new ServiceException("Failed add review", e);
        }
    }

    @Override
    public void deleteReview(Integer reviewId, Integer userId) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        Optional<Review> reviewOptional = reviewDao.findById(reviewId);

        if (!reviewOptional.isPresent()) {
            errors.put("review", "Комментарий не найден");
            throw new ValidationException(errors);
        }


        Review review = reviewOptional.get();

        if (!review.getUserId().equals(userId)) {
            errors.put("root", "У вас нет прав для удаления этого отзыва");
            throw new ValidationException(errors);
        }

        try {
            reviewDao.deleteById(reviewId);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed delete review", e);
        }

    }

    @Override
    public List<ReviewDto> getReviewsByTourId(Integer tourId) {
        try {
            Optional<TourEntity> tourOptional = tourDao.findById(tourId);

            if (!tourOptional.isPresent()) {
                return List.of();
            }

            TourEntity tour = tourOptional.get();
            Integer tourOwnerId = tour.getOperatorId();

            List<ReviewDto> reviewDtos = reviewDao.findAllByTourId(tourId);

            return reviewDtos.stream()
                    .map(review -> new ReviewDto(
                            review.id(),
                            review.userId(),
                            review.userName(),
                            review.userRole(),
                            review.userRole() == Role.OPERATOR && review.userId().equals(tourOwnerId),
                            review.text(),
                            review.createdDate()
                    ))
                    .toList();

        } catch (DataAccessException e) {
            throw new ServiceException("Failed get reviews", e);
        }
    }
}
