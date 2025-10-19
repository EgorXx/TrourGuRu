package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.OperatorSignUpDto;
import ru.kpfu.itis.sorokin.dto.UserSignUpDto;
import ru.kpfu.itis.sorokin.exception.ValidationException;

public interface OperatorService {
    void signUp(UserSignUpDto user, OperatorSignUpDto operator) throws ValidationException;
}
