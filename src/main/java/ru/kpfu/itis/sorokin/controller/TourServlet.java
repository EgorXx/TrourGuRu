package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.CardTourDto;
import ru.kpfu.itis.sorokin.dto.ReviewDto;
import ru.kpfu.itis.sorokin.dto.TourDetailDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.service.FavoriteService;
import ru.kpfu.itis.sorokin.service.ReviewService;
import ru.kpfu.itis.sorokin.service.TourService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Tours", urlPatterns = "/tours/*")
public class TourServlet extends HttpServlet {
    private TourService tourService;
    private FavoriteService favoriteService;
    private ReviewService reviewService;


    @Override
    public void init() throws ServletException {
        tourService = (TourService) getServletContext().getAttribute("tourService");
        favoriteService = (FavoriteService) getServletContext().getAttribute("favoriteService");
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        loadUserFavorites(req);

        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            showTourList(req, resp);
            return;
        } else {
            Integer tourId = getIdFromPath(path.substring(1));

            showTourDetail(req, resp, tourId);
        }
    }

    private void showTourList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CardTourDto> tourCards = tourService.getTours(1, 6);
        Boolean hasMore = tourCards.size() == 6;

        List<String> destinations = tourService.getAllTourDestinations();

        req.setAttribute("tourCards", tourCards);
        req.setAttribute("hasMore", hasMore);
        req.setAttribute("destinations", destinations);

        req.getRequestDispatcher("/tour_list.ftl").forward(req, resp);
    }

    private void showTourDetail(HttpServletRequest req, HttpServletResponse resp, Integer tourId) throws ServletException, IOException {
        TourDetailDto tourDetailDto = tourService.findById(tourId);

        List<ReviewDto> reviews = reviewService.getReviewsByTourId(tourId);

        HttpSession session = req.getSession(false);

        if (session != null) {
            Object reviewErrors = session.getAttribute("reviewErrors");
            Object oldText = session.getAttribute("oldText");

            if (reviewErrors != null) {
                req.setAttribute("reviewErrors", reviewErrors);
            }
            if (oldText != null) {
                req.setAttribute("oldText", oldText);
            }

            session.removeAttribute("reviewErrors");
            session.removeAttribute("oldText");
        }

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
        req.setAttribute("tourId", tourId);
        req.setAttribute("reviews", reviews);

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
    private void loadUserFavorites(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("user");
            List<Integer> favoriteTourIds = favoriteService.getAllFavoritesByUserId(userSessionDto.id());
            req.setAttribute("favoriteTourIds", favoriteTourIds);
        }
    }

}
