package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.Road;
import properties.MyConfig;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


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

    public static Map<String, Road> getRoadData(List<String> roadIds) {
        String searchQuery = String.format("select \"Id\", \"StartDecree\", \"EndDecree\" from \"Roads\" where \"Id\" in (%s);",
                roadIds.stream().map(v -> "?").collect(Collectors.joining(", ")));

        Map<String, Road> roads = new HashMap<>();

        try (var connection = setupConnection(); var statement = connection.prepareStatement(searchQuery)) {
            for (int i = 1; i <= roadIds.size(); i++)
                statement.setString(i, roadIds.get(i - 1));

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int start = Integer.parseInt(rs.getString("StartDecree")
                        .replaceAll("[+-]", ""));
                int end = Integer.parseInt(rs.getString("EndDecree")
                        .replaceAll("[+-]", ""));
                String id = rs.getString("Id");

                Road road = Road
                        .builder()
                        .start(start)
                        .end(end)
                        .roadId(id)
                        .build();

                roads.put(id, road);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return roads;
    }

    protected static Connection setupConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", MyConfig.DB_USER);
        properties.setProperty("password", MyConfig.DB_PASS);

        return DriverManager.getConnection(MyConfig.DB_URL, properties);
    }
}
