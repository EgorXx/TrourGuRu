package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.UserSignUpDto;
import ru.kpfu.itis.sorokin.exception.ValidationException;

public interface UserService {
    void signUp(UserSignUpDto userSignUpDto) throws ValidationException;
}