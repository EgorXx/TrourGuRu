package ru.kpfu.itis.sorokin.filter;

import ru.kpfu.itis.sorokin.dto.UserSessionDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter(urlPatterns = {"/*"})
public class AdminFilter  extends HttpFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        if (session == null) {
            chain.doFilter(request, response);
            return;
        }

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            chain.doFilter(request, response);
            return;
        }

        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.equals("/admin/operators") ||
                path.equals("/admin/operators/approve") ||
                path.equals("/admin/operators/reject") ||
                path.equals("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/operators");
    }
}
