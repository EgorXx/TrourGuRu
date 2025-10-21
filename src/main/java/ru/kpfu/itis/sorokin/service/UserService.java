package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.dto.UserSignUpDto;
import ru.kpfu.itis.sorokin.exception.ValidationException;

import java.util.Optional;

public interface UserService {
    void signUp(UserSignUpDto userSignUpDto) throws ValidationException;

    Optional<UserSessionDto> login(String email, String password) throws ValidationException;
}