package ru.kpfu.itis.sorokin.entity;

public class ServiceTour {
    private Integer id;
    private Integer tourId;
    private String title;

    public ServiceTour(Integer id, Integer tourId, String title) {
        this.id = id;
        this.tourId = tourId;
        this.title = title;
    }

    public ServiceTour() {}

    public Integer getId() {
        return id;
    }

    public Integer getTourId() {
        return tourId;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
