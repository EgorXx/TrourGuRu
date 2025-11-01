package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.exception.ValidationException;

import java.util.List;

public interface TourService {
    Integer createTour(TourCreateDto tourCreateDto, List<ImageTourAddDto> imageTourAddDtos, UserSessionDto userSessionDto) throws ValidationException;
    TourDetailDto findById(Integer tourId);
    List<CardTourDto> getTours(int page, int pageSize);
    List<CardTourDto> getToursByOperatorId(Integer operatorId);
    void deleteTour(Integer tourId, Integer operatorId) throws ValidationException;
    void updateTour(TourUpdateDto tourUpdateDto, Integer operatorId) throws ValidationException;
}
