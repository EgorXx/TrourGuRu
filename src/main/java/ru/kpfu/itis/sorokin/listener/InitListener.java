package ru.kpfu.itis.sorokin.listener;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.sorokin.dao.*;
import ru.kpfu.itis.sorokin.dao.impl.*;
import ru.kpfu.itis.sorokin.service.*;
import ru.kpfu.itis.sorokin.service.impl.*;
import ru.kpfu.itis.sorokin.util.PropertiesUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;


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
            ApplicationTourDao applicationTourDao = new ApplicationTourDaoImpl();
            FavoriteDao favoriteDao = new FavoriteDaoImpl();
            ReviewDao reviewDao = new ReviewDaoImpl();

            UserService userService = new UserServiceImpl(userDao, operatorDao);
            OperatorService operatorService = new OperatorServiceImpl(userDao, operatorDao);
            CategoryService categoryService = new CategoryServiceImpl(categoryDao);
            TourService tourService = new TourServiceImpl(tourDao, tourProgramDao, tourServiceDao, tourCategoryDao, imageUploadService, tourImageDao, operatorService);
            ApplicationTourService applicationTourService = new ApplicationTourServiceImpl(applicationTourDao, tourDao);
            FavoriteService favoriteService = new FavoriteServiceImpl(tourDao, favoriteDao);
            ReviewService reviewService = new ReviewServiceImpl(reviewDao, userDao, tourDao, operatorDao);

            ObjectMapper objectMapper = new ObjectMapper();

            sce.getServletContext().setAttribute("userService", userService);
            sce.getServletContext().setAttribute("operatorService", operatorService);
            sce.getServletContext().setAttribute("categoryService", categoryService);
            sce.getServletContext().setAttribute("tourService", tourService);
            sce.getServletContext().setAttribute("applicationTourService", applicationTourService);
            sce.getServletContext().setAttribute("objectMapper", objectMapper);
            sce.getServletContext().setAttribute("favoriteService", favoriteService);
            sce.getServletContext().setAttribute("reviewService", reviewService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
