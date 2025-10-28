package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.OperatorViewDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
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

@WebServlet(name = "ProfileEdit", urlPatterns = "/profile/edit")
public class ProfileEditServlet extends HttpServlet {
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
        HttpSession session = req.getSession();

        if (session == null) {
            resp.sendRedirect("/login");
            return;
        }

        UserSessionDto userSessionDto = (UserSessionDto) session.getAttribute("user");

        if (userSessionDto == null) {
            resp.sendRedirect("/login");
            return;
        }

        OperatorViewDto operatorViewDto;

        if (userSessionDto.isOperator()) {
            operatorViewDto = operatorService.findById(userSessionDto.id());
            req.setAttribute("operator", operatorViewDto);
        }

        req.setAttribute("user", userSessionDto);

        req.getRequestDispatcher("/profile_edit.ftl").forward(req, resp);
    }
}
