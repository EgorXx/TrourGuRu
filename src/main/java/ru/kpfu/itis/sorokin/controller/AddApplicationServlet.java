package ru.kpfu.itis.sorokin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.sorokin.dto.AddApplicationTourDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.ApplicationTourService;
import ru.kpfu.itis.sorokin.service.CategoryService;
import ru.kpfu.itis.sorokin.service.TourService;

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

@WebServlet("/application/add")
public class AddApplicationServlet extends HttpServlet {
    private ApplicationTourService applicationTourService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        applicationTourService = (ApplicationTourService) servletContext.getAttribute("applicationTourService");
        objectMapper = (ObjectMapper) servletContext.getAttribute("objectMapper");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            writeError(resp, "Необходима авторизация");

            return;
        }

        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("user");

        try {
            Map<String, Integer> request = objectMapper.readValue(
                    req.getReader(),
                    new TypeReference<Map<String, Integer>>() {}
            );

            Integer tourId = request.get("tourId");

            AddApplicationTourDto applicationTourDto = new AddApplicationTourDto(userSessionDto.id(), tourId, userSessionDto.role());
            applicationTourService.add(applicationTourDto);

            writeSuccess(resp, "Заявка успешна создана");
        }  catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(resp, "Некорректный формат данных");

        } catch (ValidationException e) {
            writeError(resp, e.getErrors().values().iterator().next());

        } catch (ServiceException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeError(resp, "Ошибка сервера");
        }
    }

    private void writeSuccess(HttpServletResponse resp, String message) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);

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
