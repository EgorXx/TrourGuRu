package ru.kpfu.itis.sorokin.entity;

public class Operator {
    private Integer userId;
    private String companyName;
    private String description;
    private Status status;

    public Operator(Integer userId, String companyName, String description, Status status) {
        this.userId = userId;
        this.companyName = companyName;
        this.description = description;
        this.status = status;
    }

    public Operator() {}

    public Integer getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
