package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Review save(Review review);

    List<Review> findAllByTourId(Integer tourId);

    void deleteById(Integer id);

    Optional<Review> findById(Integer id);
}
