package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.Role;

public record ReviewDto(
        Integer id,
        Integer userId,
        String userName,
        Role userRole,
        Boolean isOwner,
        String text,
        String createdDate
) {}
