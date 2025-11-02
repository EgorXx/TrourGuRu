package ru.kpfu.itis.sorokin.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.OperatorService;

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

@WebServlet("/admin/operators/reject")
public class AdminRejectOperatorServlet extends HttpServlet {
    private OperatorService operatorService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        operatorService = (OperatorService) servletContext.getAttribute("operatorService");
        objectMapper = (ObjectMapper) servletContext.getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            Map<String, Integer> request = objectMapper.readValue(
                    req.getReader(),
                    new TypeReference<Map<String, Integer>>() {}
            );

            Integer userId = request.get("userId");

            operatorService.rejectOperator(userId);

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
