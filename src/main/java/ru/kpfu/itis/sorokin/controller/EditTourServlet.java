package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.entity.Category;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.CategoryService;
import ru.kpfu.itis.sorokin.service.TourService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebServlet(name = "EditTour", urlPatterns = "/operator/edit-tour")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class EditTourServlet extends HttpServlet {
    private TourService tourService;
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        tourService = (TourService) servletContext.getAttribute("tourService");
        categoryService = (CategoryService) servletContext.getAttribute("categoryService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tourIdStr = req.getParameter("id");
        Integer tourId;

        try {
            tourId = Integer.parseInt(tourIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Tour ID");
            return;
        }

        TourDetailDto tour;

        try {
            tour = tourService.findById(tourId);
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Tour not found");
            return;
        }

        List<Category> categories = categoryService.findAll();

        req.setAttribute("categories", categories);
        req.setAttribute("tour", tour);

        req.getRequestDispatcher("edit-tour.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tourIdStr = req.getParameter("id");
        Integer tourId;

        try {
            tourId = Integer.parseInt(tourIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Tour ID");
            return;
        }

        Map<String, String> errors = new HashMap<>();

        String title = req.getParameter("title");
        String destination = req.getParameter("destination");
        String duration = req.getParameter("duration");
        String description = req.getParameter("description");

        String servicesString = req.getParameter("services");

        if (servicesString == null || servicesString.trim().isEmpty()) {
            servicesString = "";
        }

        String[] services = servicesString.isEmpty() ? new String[0] : servicesString.split(",");

        String[] category_ids = req.getParameterValues("category_ids");

        List<Part> parts = extractImagesPartAndValidate(req.getParts().stream().toList(), errors);

        String[] program_titles = req.getParameterValues("program_title");
        String[] program_descriptions = req.getParameterValues("program_description");
        String[] program_days = req.getParameterValues("program_day");

        validate(errors, title, destination, duration, description, services, category_ids, program_titles, program_descriptions, program_days);

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("tourTitle", title);
            req.setAttribute("tourDestination", destination);
            req.setAttribute("tourDescription", description);

            List<Category> categories = categoryService.findAll();
            req.setAttribute("categories", categories);

            req.getRequestDispatcher("edit-tour.ftl").forward(req, resp);
            return;
        }

        List<Integer> categoryIds = Arrays.stream(category_ids)
                .map(category_id -> Integer.parseInt(category_id))
                .toList();

        List<ServiceTourUpdateDto> serviceTourUpdateDtos = Arrays.stream(services)
                .map(ServiceTourUpdateDto::new)
                .toList();

        List<ProgramTourUpdateDto> programTourUpdateDtos = new ArrayList<>();

        for (int i = 0; i < program_titles.length; i++) {
            programTourUpdateDtos.add(new ProgramTourUpdateDto(
                    program_titles[i],
                    program_descriptions[i],
                    Integer.parseInt(program_days[i])
            ));
        }

        List<ImageTourUpdateDto> imageTourUpdateDtos = new ArrayList<>();

        for (Part part : parts) {
            InputStream inputStream = part.getInputStream();

            if ("main-image".equals(part.getName())) {
                imageTourUpdateDtos.add(new ImageTourUpdateDto(inputStream, true));
            } else {
                imageTourUpdateDtos.add(new ImageTourUpdateDto(inputStream, false));
            }
        }

        TourUpdateDto tourUpdateDto = new TourUpdateDto(
                tourId,
                title,
                destination,
                description,
                Integer.parseInt(duration),
                categoryIds,
                programTourUpdateDtos,
                serviceTourUpdateDtos,
                imageTourUpdateDtos
        );

        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            tourService.updateTour(tourUpdateDto, user.id());
            resp.sendRedirect(req.getContextPath() + "/tours/" + tourId);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            req.setAttribute("tourTitle", title);
            req.setAttribute("tourDestination", destination);
            req.setAttribute("tourDescription", description);

            List<Category> categories = categoryService.findAll();
            req.setAttribute("categories", categories);

            req.getRequestDispatcher("edit-tour.ftl").forward(req, resp);
            return;
        }
    }

    private void validate(
            Map<String, String> errors,
            String title,
            String destination,
            String duration,
            String description,
            String[] services,
            String[] categoryIds,
            String[] programTitles,
            String[] programDescriptions,
            String[] programDay
    ) {

        if (title == null || title.trim().isEmpty()) {
            errors.put("title", "Название тура обязательно");
        }

        if (destination == null || destination.trim().isEmpty()) {
            errors.put("destination", "Направление обязательно");
        }

        if (duration == null || duration.trim().isEmpty()) {
            errors.put("duration", "Длительность обязательна");
        } else {
            try {
                int durationInt = Integer.parseInt(duration);
                if (durationInt <= 0) {
                    errors.put("duration", "Длительность должна быть положительным числом");
                }
            } catch (NumberFormatException e) {
                errors.put("duration", "Длительность должна быть числом");
            }
        }

        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "Описание обязательно");
        }

        if (categoryIds == null || categoryIds.length == 0) {
            errors.put("category_ids", "Необходимо выбрать хотя бы одну категорию");
        }

        if (services == null || services.length == 0) {
            errors.put("services", "Необходимо указать хотя бы одну услугу");
        }

        if (programTitles == null || programTitles.length == 0) {
            errors.put("program", "Необходимо добавить хотя бы один день программы");
        } else {
            if (programDescriptions == null || programDay == null) {
                errors.put("program", "Некорректная структура программы тура");
            } else if (programTitles.length != programDescriptions.length ||
                    programTitles.length != programDay.length) {
                errors.put("program", "Несоответствие количества полей программы тура");
            }
        }
    }

    private List<Part> extractImagesPartAndValidate(List<Part> parts, Map<String, String> errors) {
        if (parts == null || parts.isEmpty()) {
            errors.put("image", "Добавьте хотя бы одно изображение");
            return null;
        }

        List<Part> images = new ArrayList<>();

        boolean hasMainImage = false;

        for (Part part : parts) {
            String fieldName = part.getName();

            if (!"main-image".equals(fieldName) && !"other-images".equals(fieldName)) {
                continue;
            }

            String fileName = part.getSubmittedFileName();

            if (fileName == null || fileName.trim().isEmpty()) {
                continue;
            }

            String contentType = part.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                continue;
            }

            images.add(part);

            if ("main-image".equals(fieldName)) {
                hasMainImage = true;
            }
        }


        if (!hasMainImage) {
            errors.put("main_image", "Необходимо загрузить главное изображение тура");
        }

        if (images.isEmpty()) {
            errors.put("image", "Добавьте хотя бы одно изображение");
        }

        return images;
    }
}
