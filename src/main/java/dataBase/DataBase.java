package dataBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DataBase {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    abstract Connection setupConnection() throws SQLException;
}
