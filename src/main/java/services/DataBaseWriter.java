package services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.IssoData;
import properties.MyConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBaseWriter {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseWriter.class);

    public static void createIssoDataTable() {
        String createTableQuery = """
                CREATE TABLE IF NOT EXISTS IssoData (
                    Id serial PRIMARY KEY,\s
                    IssoCode INT,\s
                    IssoType INT,
                    Fku VARCHAR(300),
                    Road VARCHAR(400),
                    AbdmRoadCode INT,
                    AbddRoadIds VARCHAR(500),
                    locationKm INT,
                    locationM INT,
                    objectLength INT,
                    AbddRoadIntersections VARCHAR(500)
                );""";

        try (Connection connection = setupConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void addRowToIssoDataTable(IssoData issoData) {
        String query = """
                INSERT INTO issodata(
                \tIssoCode, IssoType, Fku, Road, AbdmRoadCode, AbddRoadIds, locationKm,\s
                \tlocationM, objectLength, AbddRoadIntersections)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";

        try (var connection = setupConnection(); var statement = connection.prepareStatement(query)) {
            statement.setInt(1, issoData.getCIsso());
            statement.setInt(2, Integer.parseInt(issoData.getIssoTypeCode()));
            statement.setString(3, issoData.getOrgName());
            statement.setString(4, issoData.getDorName());
            statement.setInt(5, Integer.parseInt(issoData.getDorCode()));
            statement.setString(6, issoData.getAbddIds());
            statement.setInt(7, issoData.getKm());
            statement.setInt(8, issoData.getM());
            statement.setInt(9, Integer.parseInt(issoData.getLength()));
            statement.setString(10, issoData.getRoadsWithMatchingLen());


            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    protected static Connection setupConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", MyConfig.W_DB_USER);
        properties.setProperty("password", MyConfig.W_DB_PASS);

        return DriverManager.getConnection(MyConfig.W_DB_URL, properties);
    }
}
