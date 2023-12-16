package database;

import properties.MyConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public abstract class LogsDatabase extends DataBase {
    protected final String NO_INTERSECT_TABLE_NAME = "NoIntersectErrors";
    protected final String NO_VALUE_TABLE_NAME = "NoValueErrors";
    protected final String ISSO_DATA_TABLE_NAME = "IssoData";

    protected void truncateTable(String tableName) {
        String query = String.format("TRUNCATE %s;", tableName);

        try (Connection connection = setupConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }


    @Override
    protected Connection setupConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", MyConfig.W_DB_USER);
        properties.setProperty("password", MyConfig.W_DB_PASS);

        return DriverManager.getConnection(MyConfig.W_DB_URL, properties);
    }
}
