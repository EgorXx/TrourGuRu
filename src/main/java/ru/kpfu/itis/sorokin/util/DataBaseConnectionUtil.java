package ru.kpfu.itis.sorokin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnectionUtil {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getValue("db.url"),
                    PropertiesUtil.getValue("db.user"),
                    PropertiesUtil.getValue("db.password")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
