package fr.horizion.savoir.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig() {
        this.url = buildUrl();
        this.user = envOrDefault("DB_USER", "horizon_user");
        this.password = envOrDefault("DB_PASSWORD", "horizon_pass");
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private String buildUrl() {
        String host = envOrDefault("DB_HOST", "localhost");
        String port = envOrDefault("DB_PORT", "3306");
        String database = envOrDefault("DB_NAME", "horizon_savoir");
        return "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    }

    private String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}