package com.epam.esm.jdbc.impl;

import com.epam.esm.entity.User;
import com.epam.esm.exception.DBCPDataSourceException;
import com.epam.esm.jdbc.UserDao;
import com.epam.esm.pool.DBCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final static String SQL_COLUMN_ID = "id_user";
    private final static String SQL_COLUMN_NAME = "name_of_user";
    private final static String SQL_COLUMN_SURNAME = "surname";

    private final static String SQL_FIND_ALL_USERS = "SELECT user.id_user, user.name_of_user, user.surname FROM user ";
    private final static String SQL_SUFFIX_FOR_FIND_SPECIFIC_USER = "WHERE user.id_user = ? ";
    private final static String SQL_SUFFIX_FOR_PAGINATION = "ORDER BY id_user LIMIT ? OFFSET ?";


    @Autowired
    private DBCPDataSource dataSource;

    @Override
    public List<User> findAll(int limit, int page) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(SQL_FIND_ALL_USERS + SQL_SUFFIX_FOR_PAGINATION);
                ps.setInt(1, limit);
                ps.setInt(2, (page - 1) * limit);
                ResultSet rs = ps.executeQuery();
                return createResultList(rs);
            } catch (SQLException throwables) {
                throw new RuntimeException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<User> find(long id) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String specificRequest = createRequestForRetrieveSpecificUser();
            try {
                PreparedStatement ps = connection.prepareStatement(specificRequest);
                ps.setInt(1, (int) id);
                ps.setInt(2, 1);
                ps.setInt(3, 0);
                ResultSet rs = ps.executeQuery();
                return createResultList(rs).stream().findFirst();
            } catch (SQLException throwables) {
                throw new RuntimeException("error occurs while executing request", throwables);
            }
        } catch (DBCPDataSourceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String createRequestForRetrieveSpecificUser() {
        return SQL_FIND_ALL_USERS + SQL_SUFFIX_FOR_FIND_SPECIFIC_USER + SQL_SUFFIX_FOR_PAGINATION;
    }

    private List<User> createResultList(ResultSet rs) throws SQLException {
        List<User> resultList = new ArrayList<>();
        while (rs.next()) {
            User userFromDao = new User.UserBuilder()
                    .buildId(rs.getInt(SQL_COLUMN_ID))
                    .buildName(rs.getString(SQL_COLUMN_NAME))
                    .buildSurname(rs.getString(SQL_COLUMN_SURNAME))
                    .finishBuilding();
            resultList.add(userFromDao);
        }
        return resultList;
    }
}
