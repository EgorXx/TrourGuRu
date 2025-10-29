package ru.kpfu.itis.sorokin.entity;

public class ApplicationTour {
    private Integer id;
    private Integer userId;
    private Integer tourId;
    private Status status;

    public ApplicationTour(Integer id, Integer userId, Integer tourId, Status status) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.status = status;
    }

    public ApplicationTour(Integer userId, Integer tourId, Status status) {
        this.userId = userId;
        this.tourId = tourId;
        this.status = status;
    }

    public ApplicationTour() {}

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTourId() {
        return tourId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
