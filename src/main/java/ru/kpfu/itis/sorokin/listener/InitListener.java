package ru.kpfu.itis.sorokin.listener;

import ru.kpfu.itis.sorokin.dao.OperatorDao;
import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.dao.impl.OperatorDaoImpl;
import ru.kpfu.itis.sorokin.dao.impl.UserDaoImpl;
import ru.kpfu.itis.sorokin.service.impl.OperatorServiceImpl;
import ru.kpfu.itis.sorokin.service.impl.UserServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            UserDao userDao = new UserDaoImpl();
            OperatorDao operatorDao = new OperatorDaoImpl();
            sce.getServletContext().setAttribute("userService", new UserServiceImpl(userDao));
            sce.getServletContext().setAttribute("operatorService", new OperatorServiceImpl(userDao, operatorDao));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
