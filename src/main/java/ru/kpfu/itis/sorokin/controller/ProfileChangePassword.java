package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.UserSessionDto;
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

@WebServlet("/profile/change-password")
public class ProfileChangePassword extends HttpServlet {
    private UserService userService;
    private OperatorService operatorService;


    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        userService = (UserService) servletContext.getAttribute("userService");
        operatorService = (OperatorService) servletContext.getAttribute("operatorService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("user");

        String currentPassword = req.getParameter("currentPassword");
        String newPasswrod = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        Map<String, String> errors;

        errors = validateChangePassword(currentPassword, newPasswrod, confirmPassword);

        if (!errors.isEmpty()) {
            session.setAttribute("errors", errors);
            resp.sendRedirect(req.getContextPath() + "/profile/edit");
            return;
        }

        try {
            userService.changePassword(userSessionDto.id(), currentPassword, newPasswrod);

            session.setAttribute("success", "Пароль успешно изменен");

            resp.sendRedirect(req.getContextPath() + "/profile/edit");

        } catch (ValidationException e) {
            session.setAttribute("errors", e.getErrors());
            resp.sendRedirect(req.getContextPath() + "/profile/edit");
        }

    }

    private Map<String, String> validateChangePassword(String currentPassword, String newPasswrod, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();

        if (currentPassword == null || currentPassword.isBlank()) {
            errors.put("currentPassword", "Поле \"Текущий пароль\" не может быть пустым");
        }

        if (newPasswrod == null || newPasswrod.isBlank()) {
            errors.put("newPasswrod", "Поле \"Новый пароль\" не может быть пустым");
        }

        if (confirmPassword == null || confirmPassword.isBlank()) {
            errors.put("confirmPassword", "Поле \"Подтверждение пароля\" не может быть пустым");
        }

        if (!newPasswrod.equals(confirmPassword)) {
            errors.put("errorPassword", "Введенные пароли не совпадают");
        }

        return errors;
    }
}
