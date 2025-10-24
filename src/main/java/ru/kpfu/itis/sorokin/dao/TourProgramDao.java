package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.ProgramTour;
import ru.kpfu.itis.sorokin.entity.ServiceTour;

import java.sql.Connection;
import java.util.List;

public interface TourProgramDao {
    ProgramTour save(ProgramTour programTour, Connection connection);

    List<ProgramTour> saveAll(List<ProgramTour> programTours, Connection connection);

    List<ProgramTour> findByTourId(Integer tourId);
}
