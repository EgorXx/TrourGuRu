package ru.kpfu.itis.sorokin.controller;

import ru.kpfu.itis.sorokin.dto.UserSessionDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name="Profile", urlPatterns = "/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        req.setAttribute("user", user);
        req.getRequestDispatcher("profile.ftl").forward(req, resp);
    }
}
