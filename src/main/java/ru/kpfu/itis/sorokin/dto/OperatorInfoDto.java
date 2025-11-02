package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.Status;

public record OperatorInfoDto(
        Integer userId,
        String email,
        String companyName,
        Status status
) {}
