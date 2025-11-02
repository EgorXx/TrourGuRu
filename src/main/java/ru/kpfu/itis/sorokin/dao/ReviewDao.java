package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.Review;

import java.util.List;

public interface ReviewDao {
    Review save(Review review);

    List<Review> findAllByTourId(Integer tourId);

    void deleteById(Integer id);
}
