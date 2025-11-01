package ru.kpfu.itis.sorokin.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.FavoriteService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/favorite/toggle")
public class ToggleFavoriteServlet extends HttpServlet {
    private FavoriteService favoriteService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        favoriteService = (FavoriteService) servletContext.getAttribute("favoriteService");
        objectMapper = (ObjectMapper) servletContext.getAttribute("objectMapper");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("user");

        if (!userSessionDto.isUser()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Map<String, Integer> request = objectMapper.readValue(
                    req.getReader(),
                    new TypeReference<Map<String, Integer>>() {}
            );

            Integer tourId = request.get("tourId");

            boolean result = favoriteService.toggleFavorite(userSessionDto.id(), tourId);
            writeSuccess(resp, result);
        } catch (ValidationException e) {
            writeError(resp, e.getErrors().values().iterator().next());
        } catch (ServiceException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeError(resp, "Ошибка сервера");
        }
    }

    private void writeSuccess(HttpServletResponse resp, boolean isFavorite) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("isFavorite", isFavorite);

        String json = objectMapper.writeValueAsString(response);
        resp.getWriter().write(json);
    }

    private void writeError(HttpServletResponse resp, String message) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);

        String json = objectMapper.writeValueAsString(response);
        resp.getWriter().write(json);
    }
}
