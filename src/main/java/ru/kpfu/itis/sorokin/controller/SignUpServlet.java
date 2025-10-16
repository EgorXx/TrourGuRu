package ru.kpfu.itis.sorokin.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="SignUp", urlPatterns="/sign_up")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("contextPath", req.getContextPath());

        req.getRequestDispatcher("sign_up.ftl").forward(req, resp);
    }
}
