package ru.kpfu.itis.sorokin.dto;

public record CardTourDto(
        Integer id,
        String title,
        String destination,
        Integer duration,
        String operatorName,
        String mainImageUrl
) {}
