package ru.kpfu.itis.sorokin.listener;

import com.cloudinary.Cloudinary;
import ru.kpfu.itis.sorokin.dao.CategoryDao;
import ru.kpfu.itis.sorokin.dao.OperatorDao;
import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.dao.impl.CategoryDaoImpl;
import ru.kpfu.itis.sorokin.dao.impl.OperatorDaoImpl;
import ru.kpfu.itis.sorokin.dao.impl.UserDaoImpl;
import ru.kpfu.itis.sorokin.service.ImageUploadService;
import ru.kpfu.itis.sorokin.service.impl.CategoryServiceImpl;
import ru.kpfu.itis.sorokin.service.impl.CloudinaryImageUploadService;
import ru.kpfu.itis.sorokin.service.impl.OperatorServiceImpl;
import ru.kpfu.itis.sorokin.service.impl.UserServiceImpl;
import ru.kpfu.itis.sorokin.util.PropertiesUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebListener
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Map<String, Object> configCloudinary = new HashMap<>();
            configCloudinary.put("cloud_name", PropertiesUtil.getValue("cloudinary.cloud_name"));
            configCloudinary.put("api_key", PropertiesUtil.getValue("cloudinary.api_key"));
            configCloudinary.put("api_secret", PropertiesUtil.getValue("cloudinary.api_secret"));
            configCloudinary.put("secure", true);

            Cloudinary cloudinary = new Cloudinary(configCloudinary);

            ImageUploadService imageUploadService = new CloudinaryImageUploadService(cloudinary);

            UserDao userDao = new UserDaoImpl();
            OperatorDao operatorDao = new OperatorDaoImpl();
            CategoryDao categoryDao = new CategoryDaoImpl();

            sce.getServletContext().setAttribute("userService", new UserServiceImpl(userDao, operatorDao));
            sce.getServletContext().setAttribute("operatorService", new OperatorServiceImpl(userDao, operatorDao));
            sce.getServletContext().setAttribute("categoryService", new CategoryServiceImpl(categoryDao));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
