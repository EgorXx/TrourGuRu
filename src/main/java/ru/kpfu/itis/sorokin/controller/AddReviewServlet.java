package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.ReviewAddDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.ReviewService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/reviews/add")
public class AddReviewServlet extends HttpServlet {
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        reviewService = (ReviewService) servletContext.getAttribute("reviewService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        String text = (String) req.getParameter("text");
        String tourIdStr = req.getParameter("tourId");

        Integer tourId;

        try {
            tourId = Integer.parseInt(tourIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Tour ID");
            return;
        }

        try {
            ReviewAddDto reviewAddDto = new ReviewAddDto(text, tourId);

            reviewService.add(reviewAddDto, user);

            resp.sendRedirect(req.getContextPath() + "/tours/" + tourId);
        } catch (ValidationException e) {
            session.setAttribute("oldText", text);
            session.setAttribute("reviewErrors", e.getErrors());

            resp.sendRedirect(req.getContextPath() + "/tours/" + tourId);
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }
}
