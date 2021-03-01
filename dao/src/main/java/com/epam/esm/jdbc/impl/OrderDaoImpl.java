package com.epam.esm.jdbc.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.DBCPDataSourceException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.jdbc.OrderDao;
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
public class OrderDaoImpl implements OrderDao {

    private static final String SQL_COLUMN_ID = "id_order";
    private static final String SQL_COLUMN_ID_USER = "id_user";
    private static final String SQL_COLUMN_ID_CERT = "id_certificate";
    private static final String SQL_COLUMN_PURCHASE_TIME = "purchase_time";
    private static final String SQL_COLUMN_COST = "cost";
    private static final String SQL_COLUMN_CLOSED = "closed";

    private static final String SQL_FIND_COST_OF_CERT = "SELECT price FROM gift_certificate WHERE id_certificate = ?";
    private static final String SQL_CREATE_ORDER = "INSERT INTO order_of_user (id_user, id_certificate," +
            " cost, closed) values (?, ?, ?, ?)";
    private static final String SQL_FIND_LAST_IF_ORDER = "SELECT max(id_order) FROM order_of_user";
    private static final String SQL_FIND_ALL_ORDERS = "SELECT order_of_user.id_order, order_of_user.id_user," +
            " order_of_user.id_certificate," +
            " order_of_user.purchase_time, order_of_user.cost, order_of_user.closed" +
            " FROM order_of_user ";
    private static final String SQL_SUFFIX_FOR_FIND_SPECIFIC_ORDER = "WHERE id_order = ? ";
    private static final String SQL_SUFFIX_FOR_FIND_ORDERS_OF_SPECIFIC_USER = "WHERE id_user = ? ";
    private static final String SQL_SUFFIX_FOR_FIND_ORDER_OF_SPECIFIC_USER = "WHERE id_order = ? AND id_user = ? ";
    private static final String SQL_SUFFIX_FOR_PAGINATION = "ORDER BY id_order LIMIT ? OFFSET ?";


    @Autowired
    private DBCPDataSource dataSource;

    @Override
    public int create(Order newOrder) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement(SQL_FIND_COST_OF_CERT);
                ps.setInt(1, (int) newOrder.getIdCertificate());
                ResultSet rs = ps.executeQuery();
                long cost = 0;
                while (rs.next()) {
                    cost = rs.getLong("price");
                }
                newOrder.setCost(cost);
                ps = connection.prepareStatement(SQL_CREATE_ORDER);
                ps.setInt(1, (int) newOrder.getIdUser());
                ps.setInt(2, (int) newOrder.getIdCertificate());
                ps.setLong(3, newOrder.getCost());
                ps.setBoolean(4, false);
                ps.executeUpdate();
                ps = connection.prepareStatement(SQL_FIND_LAST_IF_ORDER);
                rs = ps.executeQuery();
                int idNewOrder = 0;
                while (rs.next()) {
                    idNewOrder = rs.getInt("id_order");
                }
                connection.commit();
                return idNewOrder;
            } catch (SQLException throwables) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new DaoException("error due to rollback");
                }
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }

        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Optional<Order> find(long id) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String specificRequest = createSpecificRequest(SQL_SUFFIX_FOR_FIND_SPECIFIC_ORDER);
            try {
                return getOrders((int) id, 1, 1, connection, specificRequest).stream().findFirst();
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
    public List<Order> findAll(int limit, int page) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_ORDERS + SQL_SUFFIX_FOR_PAGINATION);
                ps.setInt(1, limit);
                ps.setInt(2, limit * (page - 1));
                ResultSet rs = ps.executeQuery();
                return createResultList(rs);
            } catch (SQLException throwables) {
                throw new RuntimeException("error occurs while executing request");
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Order> findOrdersOfSpecificUser(long idUser, int limit, int page) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String specificRequest = createSpecificRequest(SQL_SUFFIX_FOR_FIND_ORDERS_OF_SPECIFIC_USER);
            try {
                return getOrders((int) idUser, limit, page, connection, specificRequest);
            } catch (SQLException throwables) {
                throw new DaoException("error occurs while executing request");
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public List<Order> findOrderOfSpecificUser(long idUser, long idOrder) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String specificRequest = createSpecificRequest(SQL_SUFFIX_FOR_FIND_ORDER_OF_SPECIFIC_USER);
            try {
                PreparedStatement ps = connection.prepareStatement(specificRequest);
                ps.setInt(1, (int) idOrder);
                ps.setInt(2, (int) idUser);
                ps.setInt(3, 1);
                ps.setInt(4, 0);
                ResultSet rs = ps.executeQuery();
                return createResultList(rs);
            } catch (SQLException throwables) {
                throw new DaoException("error occurs while executing request");
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private List<Order> getOrders(int idUser, int limit, int page, Connection connection, String specificRequest)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(specificRequest);
        ps.setInt(1, idUser);
        ps.setInt(2, limit);
        ps.setInt(3, limit * (page - 1));
        ResultSet rs = ps.executeQuery();
        return createResultList(rs);
    }

    private String createSpecificRequest(String variableCondition) {
        return SQL_FIND_ALL_ORDERS + variableCondition + SQL_SUFFIX_FOR_PAGINATION;
    }

    private List<Order> createResultList(ResultSet rs) throws SQLException {
        List<Order> resultList = new ArrayList<>();
        while (rs.next()) {
            Order.OrderBuilder builder = new Order.OrderBuilder();
            Order orderFromDao = builder.buildId(rs.getInt(SQL_COLUMN_ID))
                    .buildUser(rs.getInt(SQL_COLUMN_ID_USER))
                    .buildCertificates(rs.getInt(SQL_COLUMN_ID_CERT))
                    .buildCost(rs.getLong(SQL_COLUMN_COST))
                    .buildPurchaseTime(rs.getTimestamp(SQL_COLUMN_PURCHASE_TIME).toInstant())
                    .buildIsClosed(rs.getBoolean(SQL_COLUMN_CLOSED)).finishBuilding();
            resultList.add(orderFromDao);
        }
        return resultList;
    }
}
