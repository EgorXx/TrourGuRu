package ru.kpfu.itis.sorokin.entity;

public class ImageTour {
    private Integer tourId;
    private String imageUrl;
    private Boolean isMain;

    public ImageTour(Integer tourId, String imageUrl, Boolean isMain) {
        this.tourId = tourId;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }

    public ImageTour() {}

    public Integer getTourId() {
        return tourId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }
}
