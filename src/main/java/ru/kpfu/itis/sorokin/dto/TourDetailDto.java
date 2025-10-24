package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.*;

import java.util.List;

public record TourDetailDto(
        Integer id,
        String title,
        String destination,
        String description,
        Integer duration,
        Integer userId,
        String operatorCompanyName,
        String operatorDescription,
        List<Category> categories,
        List<ServiceTour> services,
        List<ProgramTour> programs,
        List<ImageTour> images
) {}

