package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.service.TourService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OperatorTours", urlPatterns = "/operator/my-tours")
public class OperatorToursServlet extends HttpServlet {
    private TourService tourService;


    @Override
    public void init() throws ServletException {
        tourService = (TourService) getServletContext().getAttribute("tourService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        try {
            List<CardTourDto> tourCards = tourService.getToursByOperatorId(user.id());
            req.setAttribute("tourCards", tourCards);
            req.getRequestDispatcher("my-tours.ftl").forward(req, resp);
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
