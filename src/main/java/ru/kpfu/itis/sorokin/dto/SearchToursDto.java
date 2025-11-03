package ru.kpfu.itis.sorokin.dto;

public record SearchToursDto(
        String search,
        String destination,
        Integer minDuration,
        Integer maxDuration
) {}
