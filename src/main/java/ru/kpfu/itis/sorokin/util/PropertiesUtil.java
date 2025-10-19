package ru.kpfu.itis.sorokin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    private PropertiesUtil() {}

    static {
        try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {

        if (input == null) {
            throw new RuntimeException("no find application.properties");
        }

        PROPERTIES.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed load properties");
        }
    }

    public static String getValue(String key) {
        return PROPERTIES.getProperty(key);
    }
}
