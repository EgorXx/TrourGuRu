package ru.kpfu.itis.sorokin.service;

import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.entity.Tour;

import java.io.InputStream;
import java.util.List;

public interface TourService {
    Tour createTour(TourCreateDto tourCreateDto, List<InputStream> imageStreams, UserSessionDto userSessionDto);
}
