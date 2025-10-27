package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.*;
import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.entity.*;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.ImageUploadService;
import ru.kpfu.itis.sorokin.service.OperatorService;
import ru.kpfu.itis.sorokin.service.TourService;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class TourServiceImpl implements TourService {
    private TourDao tourDao;
    private TourProgramDao tourProgramDao;
    private TourServiceDao tourServiceDao;
    private TourCategoryDao tourCategoryDao;
    private ImageUploadService imageUploadService;
    private TourImageDao tourImageDao;
    private OperatorService operatorService;

    public TourServiceImpl(TourDao tourDao, TourProgramDao tourProgramDao, TourServiceDao tourServiceDao,
                           TourCategoryDao tourCategoryDao, ImageUploadService imageUploadService, TourImageDao tourImageDao, OperatorService operatorService) {
        this.tourDao = tourDao;
        this.tourProgramDao = tourProgramDao;
        this.tourServiceDao = tourServiceDao;
        this.tourCategoryDao = tourCategoryDao;
        this.imageUploadService = imageUploadService;
        this.tourImageDao = tourImageDao;
        this.operatorService = operatorService;
    }

    @Override
    public Integer createTour(TourCreateDto tourCreateDto, List<ImageTourAddDto> imageTourAddDtos, UserSessionDto userSessionDto) throws ValidationException {
        validate(tourCreateDto, imageTourAddDtos);

        if (!userSessionDto.isOperator()) {
            throw new ServiceException("Недостаточно прав, чтобы создать тур");
        }

        TourEntity tourEntity = new TourEntity();

        tourEntity.setTitle(tourCreateDto.title());
        tourEntity.setOperatorId(userSessionDto.id());
        tourEntity.setDestination(tourCreateDto.destination());
        tourEntity.setDescription(tourCreateDto.description());
        tourEntity.setDuration(tourCreateDto.duration());

        List<Integer> categoryIds = tourCreateDto.categories().stream()
                .map(CategoryTourAddDto::id)
                .toList();


        List<ProgramTour> programs = tourCreateDto.programs().stream()
                .map(p -> {
                    ProgramTour programTour = new ProgramTour();
                    programTour.setTitle(p.title());
                    programTour.setDescription(p.description());
                    programTour.setDayNumber(p.dayNumber());
                    return programTour;
                }).toList();

        List<ServiceTour> services = tourCreateDto.services().stream()
                .map(s -> {
                    ServiceTour serviceTour = new ServiceTour();
                    serviceTour.setTitle(s.title());
                    return serviceTour;
                }).toList();

        List<ImageTour> images = new ArrayList<>();

        List<String> uploadedImagePublicIds = new ArrayList<>();
        Connection connection = null;

        try {
            connection = DataBaseConnectionUtil.getConnection();

            connection.setAutoCommit(false);

            TourEntity tourEntityCreated = tourDao.save(tourEntity, connection);
            Integer tourId = tourEntityCreated.getId();

            tourCategoryDao.addAll(tourId, categoryIds, connection);

            for (ProgramTour programTour : programs) {
                programTour.setTourId(tourId);
            }

            tourProgramDao.saveAll(programs, connection);

            for (ServiceTour serviceTour : services) {
                serviceTour.setTourId(tourId);
            }

            tourServiceDao.saveAll(services, connection);

            for (ImageTourAddDto imageTourAddDto : imageTourAddDtos) {
                ImageUploadResult imageUploadResult = imageUploadService.upload(imageTourAddDto.inputStream(), null);

                ImageTour image = new ImageTour();
                image.setImageUrl(imageUploadResult.secureUrl());
                image.setTourId(tourId);
                image.setMain(imageTourAddDto.isMain());
                images.add(image);

                uploadedImagePublicIds.add(imageUploadResult.publicId());
            }

            tourImageDao.saveAll(images, connection);

            connection.commit();

            return tourId;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {}
            }

            try {
                if (!uploadedImagePublicIds.isEmpty()) {
                    imageUploadService.delete(uploadedImagePublicIds);
                }
            } catch (Exception cleanEx) {}

            throw new ServiceException("Failed create Tour", e);

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {}
            }
        }
    }

    @Override
    public TourDetailDto findById(Integer tourId) {

        Optional<TourEntity> tourEntityOptional = tourDao.findById(tourId);

        if (tourEntityOptional.isEmpty()) {
            throw new ServiceException("Tour with id " + tourId + " not found");
        }

        TourEntity tourEntity = tourEntityOptional.get();

        List<Category> сategories = tourCategoryDao.findByTourId(tourId);

        if (сategories.isEmpty()) {
            throw new ServiceException("Tour with id " + tourId + " has no categories");
        }

        List<ServiceTour> services = tourServiceDao.findByTourId(tourId);

        if (services.isEmpty()) {
            throw new ServiceException("Tour with id " + tourId + " has no services");
        }

        List<ProgramTour> programs = tourProgramDao.findByTourId(tourId);

        if (programs.isEmpty()) {
            throw new ServiceException("Tour with id " + tourId + " has no programs");
        }

        List<ImageTour> images = tourImageDao.findByTourId(tourId);

        if (images.isEmpty()) {
            throw new ServiceException("Tour with id " + tourId + " has no images");
        }

        ImageDto mainImage = null;
        List<ImageDto> otherImages = new ArrayList<>();

        for (ImageTour imageTour : images) {
            if (imageTour.getMain() & mainImage == null) {
                mainImage = new ImageDto(imageTour.getImageUrl());
            } else {
                otherImages.add(new ImageDto(imageTour.getImageUrl()));
            }
        }


        OperatorViewDto operatorViewDto = operatorService.findById(tourEntity.getOperatorId());

        return new TourDetailDto(
                tourEntity.getId(),
                tourEntity.getTitle(),
                tourEntity.getDestination(),
                tourEntity.getDescription(),
                tourEntity.getDuration(),
                operatorViewDto.id(),
                operatorViewDto.companyName(),
                operatorViewDto.description(),
                mainImage,
                сategories,
                services,
                programs,
                otherImages
        );

    }

    @Override
    public List<CardTourDto> getTours(int page, int pageSize) {
        int limit = pageSize;
        int offset = (page - 1) * pageSize;
        return tourDao.findAll(limit, offset);
    }

    private void validate(TourCreateDto tourCreateDto, List<ImageTourAddDto> imageTourAddDtos) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (tourCreateDto.title() == null || tourCreateDto.title().isBlank()) {
            errors.put("tourTitle", "Название тура не может быть пустым");
        } else if (tourCreateDto.title().length() > 63) {
            errors.put("tourTitle", "Название тура не может превышать 63 символа");
        }


        if (tourCreateDto.destination() == null || tourCreateDto.destination().isBlank()) {
            errors.put("tourDestination", "Место назначения не может быть пустым");
        } else if (tourCreateDto.destination().length() > 63) {
            errors.put("tourDestination", "Место назначения не может превышать 63 символа");
        }


        if (tourCreateDto.description() == null || tourCreateDto.description().isBlank()) {
            errors.put("tourDescription", "Описание тура не может быть пустым");
        } else if (tourCreateDto.description().length() > 1023) {
            errors.put("tourDescription", "Описание тура не может превышать 1023 символа");
        }


        if (tourCreateDto.duration() == null) {
            errors.put("tourDuration", "Длительность тура должна быть указана");
        } else if (tourCreateDto.duration() <= 0) {
            errors.put("tourDuration", "Длительность тура должна быть положительным числом");
        }


        if (tourCreateDto.programs() == null || tourCreateDto.programs().isEmpty()) {
            errors.put("tourPrograms", "Программа тура должна содержать хотя бы один день");
        } else {
            for (ProgramTourAddDto programDto : tourCreateDto.programs()) {
                if (programDto.title() == null || programDto.title().isBlank()) {
                    errors.put("tourPrograms", "Название дня в программе не может быть пустым");
                    break;
                } else if (programDto.title().length() > 63) {
                    errors.put("tourPrograms", "Название дня в программе не может превышать 63 символа");
                    break;
                } else if (programDto.dayNumber() == null || programDto.dayNumber() < 1) {
                    errors.put("tourPrograms", "Номер дня в программе должен быть 1 или больше");
                    break;
                }
            }
        }


        if (tourCreateDto.services() == null || tourCreateDto.services().isEmpty()) {
            errors.put("tourServices", "Услуги тура должны содержать хотя бы одну услугу");
        } else {
            for (ServiceTourAddDto serviceDto : tourCreateDto.services()) {
                if (serviceDto.title() == null || serviceDto.title().isBlank()) {
                    errors.put("tourServices", "Название услуги не может быть пустым");
                    break;
                } else if (serviceDto.title().length() > 63) {
                    errors.put("tourServices", "Название услуги не может превышать 63 символа");
                    break;
                }
            }
        }


        if (imageTourAddDtos == null || imageTourAddDtos.isEmpty()) {
            errors.put("tourImages", "Необходимо загрузить хотя бы одно изображение для тура");
        } else {
            long mainImageCount = imageTourAddDtos.stream().filter(ImageTourAddDto::isMain).count();

            if (mainImageCount == 0) {
                errors.put("tourImages", "Необходимо указать одно главное изображение для тура");
            } else if (mainImageCount > 1) {
                errors.put("tourImages", "Главное изображение может быть только одно");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
