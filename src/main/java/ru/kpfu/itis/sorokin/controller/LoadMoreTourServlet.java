package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.dto.SearchToursDto;
import ru.kpfu.itis.sorokin.service.TourService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/tours/load-more")
public class LoadMoreTourServlet extends HttpServlet {
    private TourService tourService;


    @Override
    public void init() throws ServletException {
        tourService = (TourService) getServletContext().getAttribute("tourService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String destination = req.getParameter("destination");
        Integer minDuration = parseInteger(req.getParameter("minDuration"));
        Integer maxDuration = parseInteger(req.getParameter("maxDuration"));

        SearchToursDto searchToursDto = new SearchToursDto(search, destination, minDuration, maxDuration);
        int page = Integer.parseInt(req.getParameter("page"));

        List<CardTourDto> tourCards = tourService.getTours(searchToursDto, page, 6);

        req.setAttribute("tourCards", tourCards);

        req.getRequestDispatcher("/tour_cards_fragment.ftl").forward(req, resp);
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
