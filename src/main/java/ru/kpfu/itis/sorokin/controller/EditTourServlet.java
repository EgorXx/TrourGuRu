package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.TourDetailDto;
import ru.kpfu.itis.sorokin.entity.Category;
import ru.kpfu.itis.sorokin.service.CategoryService;
import ru.kpfu.itis.sorokin.service.TourService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EditTour", urlPatterns = "/operator/edit-tour")
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
        Integer tourId = Integer.parseInt(tourIdStr);

        TourDetailDto tour = tourService.findById(tourId);

        List<Category> categories = categoryService.findAll();

        req.setAttribute("categories", categories);
        req.setAttribute("tour", tour);

        req.getRequestDispatcher("edit-tour.ftl").forward(req, resp);
    }
}
