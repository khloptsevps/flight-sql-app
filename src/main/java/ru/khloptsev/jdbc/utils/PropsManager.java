package ru.khloptsev.jdbc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsManager {
    private static final Properties PROPS = new Properties();

    private PropsManager() {
    }

    static {
        loadProps();
    }

    private static void loadProps() {
        try (InputStream propsStream = PropsManager.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            PROPS.load(propsStream);

            if (!checkProps()) {
                throw new IOException("Проверь \"application.properties\"");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProp(String key) {
        return PROPS.getProperty(key);
    }

    private static boolean checkProps() {
        return !PROPS.getProperty("db.url").isEmpty()
                && !PROPS.getProperty("db.user").isEmpty()
                && !PROPS.getProperty("db.password").isEmpty();
    }
}
