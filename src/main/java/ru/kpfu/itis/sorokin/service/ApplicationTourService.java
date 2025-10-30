package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.AddApplicationTourDto;
import ru.kpfu.itis.sorokin.dto.UserApplicationTourDto;
import ru.kpfu.itis.sorokin.exception.ValidationException;

import java.util.List;

public interface ApplicationTourService {
    void add(AddApplicationTourDto applicationTourDto) throws ValidationException;

    List<UserApplicationTourDto> getActiveApplicationByUserId(Integer userId);
}
