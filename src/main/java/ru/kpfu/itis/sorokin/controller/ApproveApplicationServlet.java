package ru.kpfu.itis.sorokin.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.ApplicationTourService;

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

@WebServlet("/application/approve")
public class ApproveApplicationServlet extends HttpServlet {
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
        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("user");

        try {
            Map<String, Integer> request = objectMapper.readValue(
                    req.getReader(),
                    new TypeReference<Map<String, Integer>>() {}
            );

            Integer applicationId = request.get("applicationId");

            applicationTourService.approveApplicationByOperator(userSessionDto.id(), applicationId);

            writeSuccess(resp);
        } catch (ValidationException e) {
            writeError(resp, e.getErrors().values().iterator().next());
        } catch (ServiceException e) {
            writeError(resp, "Ошибка сервера");
        }
    }

    private void writeError(HttpServletResponse resp, String message) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);

        String json = objectMapper.writeValueAsString(response);
        resp.getWriter().write(json);
    }

    private void writeSuccess(HttpServletResponse resp) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        String json = objectMapper.writeValueAsString(response);
        resp.getWriter().write(json);
    }
}
