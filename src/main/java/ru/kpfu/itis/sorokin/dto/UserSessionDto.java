package ru.kpfu.itis.sorokin.dto;

import ru.kpfu.itis.sorokin.entity.Role;

public record UserSessionDto(Integer id, String displayName, String email, Role role) {
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isUser() {
        return role == Role.USER;
    }

    public boolean isOperator() {
        return role == Role.OPERATOR;
    }
}
