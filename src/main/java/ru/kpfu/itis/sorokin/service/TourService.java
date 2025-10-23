package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.entity.Tour;
import ru.kpfu.itis.sorokin.exception.ValidationException;

import java.io.InputStream;
import java.util.List;

public interface TourService {
    Integer createTour(TourCreateDto tourCreateDto, List<ImageTourAddDto> imageTourAddDtos, UserSessionDto userSessionDto) throws ValidationException;
}
