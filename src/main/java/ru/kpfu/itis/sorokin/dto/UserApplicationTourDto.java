package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.Status;

public record UserApplicationTourDto(
        Integer id,
        Integer userId,
        Status status,
        CardTourDto cardTour
) {}
