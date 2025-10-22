package ru.kpfu.itis.sorokin.entity;

public class ProgramTour {
    private Integer id;
    private Integer tourId;
    private String title;
    private String description;
    private Integer dayNumber;

    public ProgramTour(Integer id, Integer tourId, String title, String description, Integer dayNumber) {
        this.id = id;
        this.tourId = tourId;
        this.title = title;
        this.description = description;
        this.dayNumber = dayNumber;
    }

    public ProgramTour() {}

    public Integer getId() {
        return id;
    }

    public Integer getTourId() {
        return tourId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDayNumber() {
        return dayNumber;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
}
