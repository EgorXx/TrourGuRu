package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.exception.ValidationException;

import java.util.List;
import java.util.Optional;

public interface OperatorService {
    void signUp(UserSignUpDto user, OperatorSignUpDto operator) throws ValidationException;

    OperatorViewDto findById(Integer userId);

    void updateProfile(OperatorUpdateInfoDto operatorUpdateInfoDto) throws ValidationException;

    List<OperatorInfoDto> getAllOperatorsWithPendingStatus();
}
