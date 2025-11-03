package ru.kpfu.itis.sorokin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.service.FavoriteService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Favorite", urlPatterns = "/user/my-favorite")
public class UserFavoriteServlet extends HttpServlet {
    private FavoriteService favoriteService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        favoriteService = (FavoriteService) servletContext.getAttribute("favoriteService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        List<CardTourDto> tourCards = favoriteService.getFavoriteToursByUserId(user.id());

        List<Integer> favoriteTourIds = tourCards.stream()
                .map(CardTourDto::id)
                .toList();

        req.setAttribute("tourCards", tourCards);
        req.setAttribute("favoriteTourIds", favoriteTourIds);
        req.getRequestDispatcher("my-favorite.ftl").forward(req, resp);
    }
}
