package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.TourCategoryDao;
import ru.kpfu.itis.sorokin.dao.TourServiceDao;
import ru.kpfu.itis.sorokin.entity.Category;
import ru.kpfu.itis.sorokin.entity.ProgramTour;
import ru.kpfu.itis.sorokin.entity.ServiceTour;
import ru.kpfu.itis.sorokin.entity.TourEntity;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourServiceDaoImpl implements TourServiceDao {
    private static final String SQL_SAVE = "INSERT INTO service_tour (tour_id, title) VALUES (?, ?)";

    private static final String SQl_SELECT_SERVICES_BY_TOUR_ID = "SELECT id, title FROM service_tour WHERE tour_id=?";

    private static final String SQL_DELETE_BY_TOUR_ID = """
            DELETE FROM service_tour WHERE service_tour.tour_id = ?
            """;

    @Override
    public ServiceTour save(ServiceTour serviceTour, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, serviceTour.getTourId());
            preparedStatement.setString(2, serviceTour.getTitle());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save service_tour, no insert row");
            }

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    serviceTour.setId(resultSet.getInt("id"));
                    return serviceTour;
                } else {
                    throw new DataAccessException("Failed extract service_tour id");
                }

            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save service_tour", e);
        }
    }

    @Override
    public List<ServiceTour> saveAll(List<ServiceTour> serviceTours, Connection connection) {
        List<ServiceTour> services = new ArrayList<>();

        for (ServiceTour serviceTour : serviceTours) {
            ServiceTour service = save(serviceTour, connection);
            services.add(service);
        }

        return services;
    }

    @Override
    public List<ServiceTour> findByTourId(Integer tourId) {
        List<ServiceTour> services = new ArrayList<>();

        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQl_SELECT_SERVICES_BY_TOUR_ID)) {

            preparedStatement.setInt(1, tourId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ServiceTour serviceTour = new ServiceTour(
                            resultSet.getInt("id"),
                            tourId,
                            resultSet.getString("title")
                    );

                    services.add(serviceTour);
                }

                return services;

            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select services by tourId", e);
        }
    }

    @Override
    public void deleteByTourId(Connection connection, Integer tourId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_TOUR_ID)) {

            preparedStatement.setInt(1, tourId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed delete service_tour by tourId", e);
        }
    }
}
