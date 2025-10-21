package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.AuthException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.OperatorService;
import ru.kpfu.itis.sorokin.service.UserService;

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
import java.util.Optional;

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.isBlank()) {
            errors.put("email", "Поле email не может быть пустым");
        }

        if (password == null || password.isBlank()) {
            errors.put("password", "Поле \"Пароль\" не может быть пустым");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("email", email);
            req.getRequestDispatcher("login.ftl").forward(req, resp);
            return;
        }

        try {
            Optional<UserSessionDto> userSessionDtoOptional = userService.login(email, password);

            if (userSessionDtoOptional.isEmpty()) {
                req.setAttribute("formError", "Неверная почта или пароль");
                req.getRequestDispatcher("login.ftl").forward(req, resp);
            } else {
                UserSessionDto userSessionDto = userSessionDtoOptional.get();
                HttpSession httpSession = req.getSession(true);
                httpSession.setAttribute("user", userSessionDto);
                resp.sendRedirect(req.getContextPath() + "/main");
            }

        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            req.setAttribute("email", email);
            req.getRequestDispatcher("login.ftl").forward(req, resp);
        } catch (AuthException e) {
            req.setAttribute("formError", e.getMessage());
            req.getRequestDispatcher("login.ftl").forward(req, resp);
        }

    }
}
