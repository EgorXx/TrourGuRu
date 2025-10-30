package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.OperatorApplicationTourDto;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.service.ApplicationTourService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OperatorApplications", urlPatterns = "/operator/applications")
public class OperatorApplicationsServlet extends HttpServlet {
    private ApplicationTourService applicationTourService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        applicationTourService = (ApplicationTourService) servletContext.getAttribute("applicationTourService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        try {
            List<OperatorApplicationTourDto> operatorApplications = applicationTourService.getActiveApplicationByOperatorId(user.id());
            req.setAttribute("operatorApplications", operatorApplications);
            req.setAttribute("user", user);
            req.getRequestDispatcher("operator_applications.ftl").forward(req, resp);
        } catch (ServiceException e) {
            //TODO обработать ошибкку
        }
    }
}
