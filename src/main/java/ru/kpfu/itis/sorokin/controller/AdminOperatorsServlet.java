package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.OperatorInfoDto;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.service.CategoryService;
import ru.kpfu.itis.sorokin.service.OperatorService;
import ru.kpfu.itis.sorokin.service.TourService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/operators")
public class AdminOperatorsServlet extends HttpServlet {
    private OperatorService operatorService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        operatorService = (OperatorService) servletContext.getAttribute("operatorService");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<OperatorInfoDto> operators = new ArrayList<>();

        try {
            operators = operatorService.getAllOperatorsWithPendingStatus();
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }

        req.setAttribute("operators", operators);
        req.getRequestDispatcher("admin_operators.ftl").forward(req, resp);
    }
}
