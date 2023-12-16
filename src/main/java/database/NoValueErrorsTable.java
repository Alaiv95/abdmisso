package database;

import models.NoValuesErr;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NoValueErrorsTable extends LogsDatabase {
    public void createEmptyNoValErrTable() {
        String createTableQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                    Id serial PRIMARY KEY,
                    Message VARCHAR(150),
                    IssoCode INT,
                    UNIQUE(IssoCode)
                );""", NO_VALUE_TABLE_NAME);

        try (Connection connection = setupConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
            truncateTable(NO_VALUE_TABLE_NAME);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }


    public void addRow(NoValuesErr noValuesErr) {
        String query = "INSERT INTO " + NO_VALUE_TABLE_NAME + " (Message, IssoCode) VALUES (?, ?);";

        try (var connection = setupConnection(); var statement = connection.prepareStatement(query)) {
            statement.setString(1, noValuesErr.getMessage());
            statement.setInt(2, Integer.parseInt(noValuesErr.getIssoCode()));

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

}
