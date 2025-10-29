package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.ApplicationTourDao;
import ru.kpfu.itis.sorokin.dao.TourDao;
import ru.kpfu.itis.sorokin.dao.TourServiceDao;
import ru.kpfu.itis.sorokin.dto.AddApplicationTourDto;
import ru.kpfu.itis.sorokin.entity.ApplicationTour;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.Status;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.DuplicateApplicationException;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.ApplicationTourService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationTourServiceImpl implements ApplicationTourService {
    private ApplicationTourDao applicationTourDao;
    private TourDao tourDao;

    public ApplicationTourServiceImpl(ApplicationTourDao applicationTourDao, TourDao tourDao) {
        this.applicationTourDao = applicationTourDao;
        this.tourDao = tourDao;
    }

    @Override
    public void add(AddApplicationTourDto applicationTourDto) throws ValidationException {
        Integer userId = applicationTourDto.userId();
        Integer tourId = applicationTourDto.tourId();

        Map<String, String> errors = new HashMap<>();

        if (applicationTourDto.role() == Role.OPERATOR) {
            errors.put("role", "Туроператор не может оставить заявку на тур");
            throw new ValidationException(errors);
        }

        if (!tourDao.findById(tourId).isPresent()) {
            errors.put("tour", "Тур не найден");
            throw new ValidationException(errors);
        }

        ApplicationTour applicationTour = new ApplicationTour(userId, tourId, Status.PENDING);

        try {
            applicationTourDao.save(applicationTour);

        } catch (DuplicateApplicationException e) {
            ApplicationTour applicationExist = applicationTourDao.findByUserIdAndTourId(userId, tourId)
                    .orElseThrow(() -> new ServiceException("duplicate detected but not found"));;

            Status status = applicationExist.getStatus();

            if (status == Status.PENDING) {
                errors.put("status", "Заявка в обработке");
            } else if (status == Status.APPROVED) {
                errors.put("status", "Заявка уже принита");
            }

            throw new ValidationException(errors);

        } catch (DataAccessException e) {
            throw new ServiceException("Failed add application tour");
        }
    }
}
