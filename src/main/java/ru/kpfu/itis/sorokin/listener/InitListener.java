package ru.kpfu.itis.sorokin.listener;

import com.cloudinary.Cloudinary;
import ru.kpfu.itis.sorokin.dao.*;
import ru.kpfu.itis.sorokin.dao.impl.*;
import ru.kpfu.itis.sorokin.service.ImageUploadService;
import ru.kpfu.itis.sorokin.service.impl.*;
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
            TourCategoryDao tourCategoryDao = new TourCategoryDaoImpl();
            TourDao tourDao = new TourDaoImpl();
            TourImageDao tourImageDao = new TourImageDaoImpl();
            TourProgramDao tourProgramDao = new TourProgramDaoImpl();
            TourServiceDao tourServiceDao = new TourServiceDaoImpl();

            sce.getServletContext().setAttribute("userService", new UserServiceImpl(userDao, operatorDao));
            sce.getServletContext().setAttribute("operatorService", new OperatorServiceImpl(userDao, operatorDao));
            sce.getServletContext().setAttribute("categoryService", new CategoryServiceImpl(categoryDao));
            sce.getServletContext().setAttribute("tourService", new TourServiceImpl(tourDao, tourProgramDao, tourServiceDao, tourCategoryDao, imageUploadService, tourImageDao));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
