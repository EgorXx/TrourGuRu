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
public class RoleFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        UserSessionDto user = (UserSessionDto) req.getSession().getAttribute("user");

        if (path.startsWith("/admin/") && !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (isOperatorPath(path) && !user.isOperator()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (isUserPath(path) && !user.isUser()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isUserPath(String path) {
        return path.startsWith("/user/") || path.startsWith("/application/add")
                || path.startsWith("/application/cancel") || path.startsWith("/favorite/toggle");
    }

    private boolean isOperatorPath(String path) {
        return path.startsWith("/operator/") || path.startsWith("/application/approve")
                || path.startsWith("/application/reject") || path.startsWith("/tour/delete");
    }
}
