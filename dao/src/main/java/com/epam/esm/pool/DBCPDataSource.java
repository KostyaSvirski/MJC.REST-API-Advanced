package com.epam.esm.pool;

import com.epam.esm.exception.DBCPDataSourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBCPDataSource {

   @Autowired
   private DataSource dataSource;

    public Connection getConnection() throws DBCPDataSourceException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DBCPDataSourceException("can't get connection", e.getCause());
        }
    }

    public void closeConnection(Connection connection) throws DBCPDataSourceException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DBCPDataSourceException("can't close connection", e.getCause());
        }

    }

}
