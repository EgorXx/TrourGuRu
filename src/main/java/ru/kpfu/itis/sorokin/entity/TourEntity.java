package ru.kpfu.itis.sorokin.entity;

public class TourEntity {
    private Integer id;
    private String title;
    private Integer operatorId;
    private String destination;
    private String description;
    private Integer duration;

    public TourEntity(Integer id, String title, Integer operatorId, String destination, String description, Integer duration) {
        this.id = id;
        this.title = title;
        this.operatorId = operatorId;
        this.destination = destination;
        this.description = description;
        this.duration = duration;
    }

    public TourEntity() {}

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
