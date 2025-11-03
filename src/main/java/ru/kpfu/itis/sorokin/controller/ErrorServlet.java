package ru.kpfu.itis.sorokin.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/error/404", "/error/403", "/error/500"})
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        String errorCode = path.substring(path.lastIndexOf("/") + 1);

        req.setAttribute("errorCode", errorCode);

        String message = switch (errorCode) {
            case "404" -> "Страница не найдена";
            case "403" -> "Доступ запрещён";
            case "500" -> "Ошибка сервера";
            default -> "Что-то пошло не так";
        };
        req.setAttribute("errorMessage", message);
        req.setAttribute("contextPath", req.getContextPath());

        req.getRequestDispatcher("/error.ftl").forward(req, resp);
    }
}

