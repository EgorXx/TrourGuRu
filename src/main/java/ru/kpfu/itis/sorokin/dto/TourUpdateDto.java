package ru.kpfu.itis.sorokin.dto;

import java.util.List;

public record TourUpdateDto(
        Integer tourId,
        String title,
        String destination,
        String description,
        Integer duration,
        List<Integer> categoryIds,
        List<ProgramTourUpdateDto> programs,
        List<ServiceTourUpdateDto> services,
        List<ImageTourUpdateDto> images
) {}
