package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.Status;

public record OperatorApplicationTourDto(
        Integer applicationId,
        Status status,
        String userEmail,
        String userName,
        String tourTitle,
        String tourDestination,
        Integer tourDuration
) {}
