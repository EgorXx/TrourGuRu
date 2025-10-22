package ru.kpfu.itis.sorokin.entity;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private Integer id;
    private String title;
    private Integer operatorId;
    private String destination;
    private String description;
    private Integer duration;

    private List<Category> categories;
    private List<ServiceTour> services;
    private List<ProgramTour> programs;
    private List<ImageTour> images;

    public Tour() {
        categories = new ArrayList<>();
        services = new ArrayList<>();
        programs = new ArrayList<>();
        images = new ArrayList<>();
    }

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

    public List<Category> getCategories() {
        return categories;
    }

    public List<ServiceTour> getServices() {
        return services;
    }

    public List<ProgramTour> getPrograms() {
        return programs;
    }

    public List<ImageTour> getImages() {
        return images;
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

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setServices(List<ServiceTour> services) {
        this.services = services;
    }

    public void setPrograms(List<ProgramTour> programs) {
        this.programs = programs;
    }

    public void setImages(List<ImageTour> images) {
        this.images = images;
    }
}
