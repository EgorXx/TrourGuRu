package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.AddApplicationTourDto;
import ru.kpfu.itis.sorokin.exception.ValidationException;

public interface ApplicationTourService {
    void add(AddApplicationTourDto applicationTourDto) throws ValidationException;
}
