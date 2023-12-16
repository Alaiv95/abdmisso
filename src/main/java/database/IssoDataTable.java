package database;


import models.IssoData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class IssoDataTable extends LogsDatabase {

    public void createEmptyIssoDataTable() {

        String createTableQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                    Id serial PRIMARY KEY,
                    IssoCode INT UNIQUE,
                    IssoType INT,
                    Fku VARCHAR(300),
                    Road VARCHAR(400),
                    AbdmRoadCode INT,
                    AbddRoadIds VARCHAR(500),
                    LocationKm INT,
                    LocationM INT,
                    ObjectLength VARCHAR(500),
                    AbddRoadIntersections VARCHAR(500),
                    Identifier VARCHAR(150)
                );""", ISSO_DATA_TABLE_NAME);

        try (Connection connection = setupConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
            truncateTable(ISSO_DATA_TABLE_NAME);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void addRow(IssoData issoData) {
        String query = "INSERT INTO " + ISSO_DATA_TABLE_NAME + """
                 (IssoCode, IssoType, Fku, Road, AbdmRoadCode, AbddRoadIds, LocationKm,
                 LocationM, ObjectLength, AbddRoadIntersections, Identifier)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";

        try (var connection = setupConnection(); var statement = connection.prepareStatement(query)) {
            String identifier = String.format("%s_%s_%s", issoData.getDorCode(),
                    issoData.getKm(), issoData.getM());

            statement.setInt(1, issoData.getCIsso());
            statement.setInt(2, Integer.parseInt(issoData.getIssoTypeCode()));
            statement.setString(3, issoData.getOrgName());
            statement.setString(4, issoData.getDorName());
            statement.setInt(5, Integer.parseInt(issoData.getDorCode()));
            statement.setString(6, issoData.getAbddIds());
            statement.setInt(7, issoData.getKm());
            statement.setInt(8, issoData.getM());
            statement.setString(9, issoData.getLength());
            statement.setString(10, issoData.getRoadsWithMatchingLen());
            statement.setString(11, identifier);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
