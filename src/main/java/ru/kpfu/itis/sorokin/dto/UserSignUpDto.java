package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.Role;

public record UserSignUpDto(String email, String username, String password, Role role) {}
