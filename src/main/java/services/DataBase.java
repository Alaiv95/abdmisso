package services;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import properties.MyConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DataBase {
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);

    public static List<Integer> getAllExistingObjectsIsso() {
        String searchQuery = "select \"Payload\"->>'numberISSO' code from \"Cards\" where \"Type\" in (29, 36, 74, 75);";
        List<Integer> codes = new ArrayList<>();

        try (Connection connection = setupConnection(); Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(searchQuery)) {
            while (rs.next()) {
                int issoCode = rs.getInt("code");
                codes.add(issoCode);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return codes;
    }

    protected static Connection setupConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", MyConfig.DB_USER);
        properties.setProperty("password", MyConfig.DB_PASS);

        return DriverManager.getConnection(MyConfig.DB_URL, properties);
    }
}
