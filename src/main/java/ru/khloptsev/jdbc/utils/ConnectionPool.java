package ru.khloptsev.jdbc.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.khloptsev.jdbc.exceptions.ConnectionException;
import ru.khloptsev.jdbc.exceptions.DatabaseInitException;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionPool {
    private ConnectionPool() {
    }
    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";

    private static HikariDataSource dataSource;


    static {
        try {
            initDataSource();
        } catch (Exception e) {
            throw new DatabaseInitException("Ошибка инициализации пула. Проверь URL, username, password", e);
        }
    }

    private static HikariConfig initConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropsManager.getProp(URL_KEY));
        config.setUsername(PropsManager.getProp(USER_KEY));
        config.setPassword(PropsManager.getProp(PASSWORD_KEY));
        config.setLeakDetectionThreshold(5000); // 5 секунд
        config.setIdleTimeout(60000);
        config.setMaximumPoolSize(5);
        return config;
    }

    private static void initDataSource() {
        dataSource = new HikariDataSource(initConfig());
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionException("Не возможно взять соединение из пула", e);
        }
    }
}
