package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourProgramDao;
import ru.kpfu.itis.sorokin.entity.ProgramTour;
import ru.kpfu.itis.sorokin.entity.ServiceTour;
import ru.kpfu.itis.sorokin.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourProgramDaoImpl implements TourProgramDao {
    private static final String SQL_SAVE = "INSERT INTO program_tour (tour_id, title, description, day_number) VALUES (?, ?, ?, ?)";

    @Override
    public ProgramTour save(ProgramTour programTour, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, programTour.getTourId());
            preparedStatement.setString(2, programTour.getTitle());
            preparedStatement.setString(3, programTour.getDescription());
            preparedStatement.setInt(4, programTour.getDayNumber());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save program_tour, no insert row");
            }

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    programTour.setId(resultSet.getInt("id"));
                    return programTour;
                } else {
                    throw new DataAccessException("Failed extract program_tour id");
                }

            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save program_tour", e);
        }
    }

    @Override
    public List<ProgramTour> saveAll(List<ProgramTour> programTours, Connection connection) {
        List<ProgramTour> programs = new ArrayList<>();

        for (ProgramTour programTour : programTours) {
            ProgramTour program = save(programTour, connection);
            programs.add(program);
        }

        return programs;
    }

}
