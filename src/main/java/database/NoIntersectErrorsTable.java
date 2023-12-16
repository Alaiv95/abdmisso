package database;

import models.NoIntersectErr;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NoIntersectErrorsTable extends LogsDatabase {
    public void createEmptyNoIntersectErrTable() {

        String createTableQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                    Id serial PRIMARY KEY,\s
                    Message VARCHAR(150),\s
                    AbdmRoadCode INT,
                    LocationKm INT,
                    LocationM INT,
                    Identifier VARCHAR(150)
                );""", NO_INTERSECT_TABLE_NAME);

        try (Connection connection = setupConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
            truncateTable(NO_INTERSECT_TABLE_NAME);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }


    public void addRow(NoIntersectErr noIntersectErr) {
        String query = "INSERT INTO " + NO_INTERSECT_TABLE_NAME +
                " (Message, AbdmRoadCode, LocationKm, LocationM, Identifier) VALUES (?, ?, ?, ?, ?);";

        try (var connection = setupConnection(); var statement = connection.prepareStatement(query)) {
            String identifier = String.format("%s_%s_%s", noIntersectErr.getRoadCode(),
                    noIntersectErr.getKm(), noIntersectErr.getM());

            statement.setString(1, noIntersectErr.getMessage());
            statement.setInt(2, Integer.parseInt(noIntersectErr.getRoadCode()));
            statement.setInt(3, Integer.parseInt(noIntersectErr.getKm()));
            statement.setInt(4, Integer.parseInt(noIntersectErr.getM()));
            statement.setString(5 , identifier);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
