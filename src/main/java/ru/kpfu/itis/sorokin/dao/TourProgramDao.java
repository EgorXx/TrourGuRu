package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.ProgramTour;

import java.sql.Connection;

public interface TourProgramDao {
    ProgramTour save(ProgramTour programTour, Connection connection);
}
