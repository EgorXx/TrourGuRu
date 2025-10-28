package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.OperatorUpdateInfoDto;
import ru.kpfu.itis.sorokin.dto.OperatorViewDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ProfileUpdate", urlPatterns = "/profile/update")
public class ProfileUpdateServlet extends HttpServlet {
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

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("user");

        if (userSessionDto == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Map<String, String> errors;

        if (userSessionDto.isUser()) {
            Integer userId = userSessionDto.id();
            String userName = req.getParameter("name");
            String email = req.getParameter("email");
            errors = validateUserInfo(userName, email);

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.setAttribute("user", userSessionDto);
                req.getRequestDispatcher("/profile_edit.ftl").forward(req, resp);
                return;
            }

            try {
                userService.updateProfile(userId, userName, email);
            } catch (ValidationException e) {
                req.setAttribute("errors", e.getErrors());
                req.setAttribute("user", userSessionDto);
                req.getRequestDispatcher("/profile_edit.ftl").forward(req, resp);
                return;
            }

            UserSessionDto newUserSessionDto = new UserSessionDto(userId, userName, email, Role.USER);
            session.setAttribute("user", newUserSessionDto);
            resp.sendRedirect(req.getContextPath() + "/profile/edit?success=true");

        } else if (userSessionDto.isOperator()) {
            Integer userId = userSessionDto.id();
            String companyName = req.getParameter("companyName");
            String description = req.getParameter("description");
            String email = req.getParameter("email");
            errors = validateOperatorInfo(companyName, email);

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.setAttribute("user", userSessionDto);
                OperatorViewDto operatorViewDto = operatorService.findById(userSessionDto.id());
                req.setAttribute("operator", operatorViewDto);
                req.getRequestDispatcher("/profile_edit.ftl").forward(req, resp);
                return;
            }

            OperatorUpdateInfoDto operatorUpdateInfoDto = new OperatorUpdateInfoDto(
                    userId,
                    email,
                    companyName,
                    description);

            try {
                operatorService.updateProfile(operatorUpdateInfoDto);
            } catch (ValidationException e) {
                req.setAttribute("errors", e.getErrors());
                req.setAttribute("user", userSessionDto);
                OperatorViewDto operatorViewDto = operatorService.findById(userSessionDto.id());
                req.setAttribute("operator", operatorViewDto);
                req.getRequestDispatcher("/profile_edit.ftl").forward(req, resp);
                return;
            }

            UserSessionDto newUserSessionDto = new UserSessionDto(userId, companyName, email, Role.OPERATOR);
            session.setAttribute("user", newUserSessionDto);
            resp.sendRedirect(req.getContextPath() + "/profile/edit?success=true");
        }
    }

    private Map<String, String>  validateUserInfo(String userName, String email) {
        Map<String, String> errors = new HashMap<>();

        if (userName == null || userName.isBlank()) {
            errors.put("userName", "Поле \"Имя пользователя\" не может быть пустым");
        }

        if (email == null || email.isBlank()) {
            errors.put("email", "Поле email не может быть пустым");
        }

        return errors;
    }

    private Map<String, String>  validateOperatorInfo(String companyName, String email) {
        Map<String, String> errors = new HashMap<>();

        if (companyName == null || companyName.isBlank()) {
            errors.put("companyName", "Поле \"Название компании\" не может быть пустым");
        }

        if (email == null || email.isBlank()) {
            errors.put("email", "Поле email не может быть пустым");
        }

        return errors;
    }
}
