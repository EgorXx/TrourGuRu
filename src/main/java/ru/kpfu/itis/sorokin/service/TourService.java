package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.entity.Tour;

import javax.servlet.http.Part;
import java.util.List;

public interface TourService {
    Tour createTour(TourCreateDto tourCreateDto, List<Part> imageParts, UserSessionDto userSessionDto);
}
