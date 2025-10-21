package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.OperatorSignUpDto;
import ru.kpfu.itis.sorokin.dto.UserSignUpDto;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.OperatorService;
import ru.kpfu.itis.sorokin.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name="SignUp", urlPatterns="/sign_up")
public class SignUpServlet extends HttpServlet {

    private UserService userService;
    private OperatorService operatorService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        userService = (UserService) servletContext.getAttribute("userService");
        operatorService = (OperatorService) servletContext.getAttribute("operatorService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("sign_up.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();

        String email = req.getParameter("email");
        String username = null;
        String companyName = null;
        String password = req.getParameter("password");
        Role role = Role.valueOf(req.getParameter("role"));

        if (email == null || email.isBlank()) {
            errors.put("email", "Поле email не может быть пустым");
        }

        if (password == null || password.isBlank()) {
            errors.put("password", "Поле password не может быть пустым");
        } else if (password.length() < 8) {
            errors.put("password", "Пароль слишком короткий (минимальная длина: 8)");
        }


        if (role == Role.USER) {
            username = req.getParameter("username");

            if (username == null || username.isBlank()) {
                errors.put("username", "Поле \"Имя пользователя\" не может быть пустым");
            }

        } else if (role == Role.OPERATOR) {
            companyName = req.getParameter("company_name");

            if (companyName == null || companyName.isBlank()) {
                errors.put("company_name", "Поле \"Название компании\" не может быть пустым");
            }

        } else {
            errors.put("role", "Выбрана недопустимая роль");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("username", username);
            req.setAttribute("company_name", companyName);
            req.setAttribute("email", email);
            req.getRequestDispatcher("sign_up.ftl").forward(req, resp);
            return;
        }

        UserSignUpDto userSignUpDto = new UserSignUpDto(email, username, password);

        OperatorSignUpDto operatorSignUpDto = new OperatorSignUpDto(companyName);


        try {
            if (role == Role.USER) {
                userService.signUp(userSignUpDto);
            } else {
                operatorService.signUp(userSignUpDto, operatorSignUpDto);
            }

            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (ValidationException e) {
            Map<String, String> serviceErrors = e.getErrors();

            req.setAttribute("errors", serviceErrors);
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.getRequestDispatcher("sign_up.ftl").forward(req, resp);
        }
    }

}
