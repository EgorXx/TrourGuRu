package ru.kpfu.itis.sorokin.dto;

import java.util.List;

public record TourCreateDto(
        String title,
        String destination,
        String description,
        Integer duration,

        List<CategoryTourAddDto> categories,
        List<ProgramTourAddDto> programs,
        List<ServiceTourAddDto> services
) {}
