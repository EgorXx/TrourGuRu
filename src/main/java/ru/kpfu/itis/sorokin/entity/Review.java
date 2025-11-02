package ru.kpfu.itis.sorokin.entity;

import java.time.LocalDateTime;

public class Review {
    private Integer id;
    private String text;
    private Integer userId;
    private Integer tourId;
    private LocalDateTime createdTime;

    public Review(Integer id, String text, Integer userId, Integer tourId, LocalDateTime createdTime) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.tourId = tourId;
        this.createdTime = createdTime;
    }

    public Review() {}

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTourId() {
        return tourId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
