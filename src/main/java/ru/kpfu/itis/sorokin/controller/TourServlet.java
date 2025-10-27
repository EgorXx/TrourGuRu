package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.TourDetailDto;
import ru.kpfu.itis.sorokin.entity.Tour;
import ru.kpfu.itis.sorokin.service.TourService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Tours", urlPatterns = "/tours/*")
public class TourServlet extends HttpServlet {
    private TourService tourService;


    @Override
    public void init() throws ServletException {
        tourService = (TourService) getServletContext().getAttribute("tourService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            //showTourList();
            return;
        }

        Integer tourId = getIdFromPath(path.substring(1));

        showTourDetail(req, resp, tourId);
    }

    private void showTourDetail(HttpServletRequest req, HttpServletResponse resp, Integer tourId) throws ServletException, IOException {
        TourDetailDto tourDetailDto = tourService.findById(tourId);

        req.setAttribute("tourTitle", tourDetailDto.title());
        req.setAttribute("destination", tourDetailDto.destination());
        req.setAttribute("description", tourDetailDto.description());
        req.setAttribute("duration", tourDetailDto.duration());
        req.setAttribute("operatorCompanyName", tourDetailDto.operatorCompanyName());
        req.setAttribute("operatorDescription", tourDetailDto.operatorDescription());
        req.setAttribute("categories", tourDetailDto.categories());
        req.setAttribute("services", tourDetailDto.services());
        req.setAttribute("programs", tourDetailDto.programs());
        req.setAttribute("mainImage", tourDetailDto.mainImage());
        req.setAttribute("otherImages", tourDetailDto.otherImages());

        System.out.println(tourDetailDto.operatorCompanyName());
        System.out.println(tourDetailDto.operatorDescription());

        req.getRequestDispatcher("/tour_detail.ftl").forward(req, resp);
    }

    private Integer getIdFromPath(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            //TODO: обработка исключения
            throw new RuntimeException(e);
        }

    }
}
