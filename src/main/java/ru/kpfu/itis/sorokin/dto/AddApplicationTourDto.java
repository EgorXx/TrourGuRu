package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.Role;

public record AddApplicationTourDto(Integer userId, Integer tourId, Role role) {}
