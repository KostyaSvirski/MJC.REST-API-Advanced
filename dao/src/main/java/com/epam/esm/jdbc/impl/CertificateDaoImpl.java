package com.epam.esm.jdbc.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DBCPDataSourceException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.jdbc.GiftCertificateDao;
import com.epam.esm.pool.DBCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements GiftCertificateDao {

    private static final String SQL_COLUMN_ID_TAG = "id_tag";
    private static final String SQL_COLUMN_NAME_TAG = "name_of_tag";
    private static final String SQL_COLUMN_ID_CERTIFICATE = "id_certificate";
    private static final String SQL_COLUMN_NAME_CERTIFICATE = "name_of_certificate";
    private static final String SQL_COLUMN_DESCRIPTION = "description";
    private static final String SQL_COLUMN_PRICE = "price";
    private static final String SQL_COLUMN_DURATION = "duration";
    private static final String SQL_COLUMN_CREATE_DATE = "create_date";
    private static final String SQL_COLUMN_LAST_UPDATE_DATE = "last_update_date";

    private static final String SQL_CREATE_CERTIFICATE = "insert into gift_certificate (name_of_certificate," +
            " description, price," +
            " duration, create_date, last_update_date) values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_CREATE_JUNCTIONS_WITH_TAGS = "insert into junction_gift_certificates_and_tags " +
            "(id_certificate, id_tag) values (?, ?)";
    private static final String SQL_FIND_ID_SPECIFIC_TAG = "select id_tag from tag_for_certificates " +
            "where id_tag = ?";
    private static final String SQL_FIND_LAST_ID = "select max(id_certificate) from gift_certificate";
    private static final String SQL_DELETE_CERTIFICATE = "delete from gift_certificate where id_certificate = ?";
    private static final String SQL_DELETE_JUNCTIONS = "delete from junction_gift_cerficates_and_tags " +
            "where id_certificate = ?";
    private static final String SQL_UPDATE_CERTIFICATE = "update gift_certificate set" +
            " name_of_certificate = ?, description = ?," +
            " price = ?, duration = ? where id_certificate = ?";
    private static final String SQL_FIND_ALL_CERTIFICATES = "select gift_certificate.id_certificate," +
            " gift_certificate.name_of_certificate, description, price, duration, create_date, last_update_date," +
            " tag_for_certificates.id_tag," +
            " tag_for_certificates.name_of_tag from gift_certificate" +
            " inner join junction_gift_certificates_and_tags on gift_certificate.id_certificate =" +
            " junction_gift_certificates_and_tags.id_certificate" +
            " inner join tag_for_certificates on junction_gift_certificates_and_tags.id_tag =" +
            " tag_for_certificates.id_tag ";
    private static final String SQL_FIND_SPECIFIC_CERTIFICATE = "select gift_certificate.id_certificate," +
            " gift_certificate.name_of_certificate, description, price, duration, create_date, last_update_date," +
            " tag_for_certificates.id_tag," +
            " tag_for_certificates.name_of_tag from gift_certificate" +
            " inner join junction_gift_certificates_and_tags on gift_certificate.id_certificate =" +
            " junction_gift_certificates_and_tags.id_certificate" +
            " inner join tag_for_certificates on junction_gift_certificates_and_tags.id_tag = " +
            " tag_for_certificates.id_tag where gift_certificate.id_certificate = ? ";
    private static final String SQL_SEARCH_BY_PART_OF_NAME = "select gift_certificate.id_certificate," +
            " gift_certificate.name_of_certificate, description, price, duration, create_date, last_update_date," +
            " tag_for_certificates.id_tag," +
            " tag_for_certificates.name_of_tag from gift_certificate" +
            " inner join junction_gift_certificates_and_tags on gift_certificate.id_certificate =" +
            " junction_gift_certificates_and_tags.id_certificate" +
            " inner join tag_for_certificates on junction_gift_certificates_and_tags.id_tag = " +
            " tag_for_certificates.id_tag where gift_certificate.name like ";
    private static final String SQL_SEARCH_BY_PART_OF_DESCRIPTION = "select gift_certificate.id_certificate," +
            " gift_certificate.name_of_certificate, description, price, duration, create_date, last_update_date," +
            " tag_for_certificates.id_tag," +
            " tag_for_certificates.name_of_tag from gift_certificate" +
            " inner join junction_gift_certificates_and_tags on gift_certificate.id_certificate =" +
            " junction_gift_certificates_and_tags.id_certificate" +
            " inner join tag_for_certificates on junction_gift_certificates_and_tags.id_tag = " +
            " tag_for_certificates.id_tag where gift_certificate.description like ";
    private static final String SQL_SORT_CERTIFICATES = "select gift_certificate.id_certificate," +
            " gift_certificate.name_of_certificate, description, price, duration, create_date, last_update_date," +
            " tag_for_certificates.id_tag," +
            " tag_for_certificates.name_of_tag from gift_certificate" +
            " inner join junction_gift_certificates_and_tags on gift_certificate.id_certificate =" +
            " junction_gift_certificates_and_tags.id_certificate" +
            " inner join tag_for_certificates on junction_gift_certificates_and_tags.id_tag = " +
            " tag_for_certificates.id_tag order by ";
    private static final String SQL_FIND_BY_TAGS = "select gift_certificate.id_certificate," +
            " gift_certificate.name_of_certificate, description, price, duration, create_date, last_update_date," +
            " tag_for_certificates.id_tag," +
            " tag_for_certificates.name_of_tag from gift_certificate" +
            " inner join junction_gift_certificates_and_tags on gift_certificate.id_certificate =" +
            " junction_gift_certificates_and_tags.id_certificate" +
            " inner join tag_for_certificates on junction_gift_certificates_and_tags.id_tag = " +
            " tag_for_certificates.id_tag where tag_for_certificates.name = ? ";
    private static final String SQL_SUFFIX_FOR_PAGINATION = "ORDER BY id_certificate LIMIT ? OFFSET ?";

    @Autowired
    private DBCPDataSource dataSource;

    @Override
    public int create(GiftCertificate certificate) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_CERTIFICATE);
                ps.setString(1, certificate.getName());
                ps.setString(2, certificate.getDescription());
                ps.setLong(3, certificate.getPrice());
                ps.setDate(4, Date.valueOf(certificate.getDuration().addTo(LocalDate.now()).toString()));
                ps.setTimestamp(5, Timestamp.from(certificate.getCreateDate()));
                ps.setTimestamp(6, Timestamp.from(certificate.getLastUpdateDate()));
                ps.executeUpdate();
                ps = connection.prepareStatement(SQL_FIND_ID_SPECIFIC_TAG);
                for (Tag tag : certificate.getTagsDependsOnCertificate()) {
                    ps.setInt(1, (int) tag.getId());
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        connection.rollback();
                        throw new DaoException("tag no found");
                    }
                }
                ps = connection.prepareStatement(SQL_FIND_LAST_ID);
                ResultSet rs = ps.executeQuery();
                int id = 0;
                while (rs.next()) {
                    id = rs.getInt(1);
                }
                ps = connection.prepareStatement(SQL_CREATE_JUNCTIONS_WITH_TAGS);
                for (Tag tag : certificate.getTagsDependsOnCertificate()) {
                    ps.setInt(1, id);
                    ps.setInt(2, (int) tag.getId());
                    ps.executeUpdate();
                }
                connection.commit();
                return id;
            } catch (SQLException throwables) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new DaoException("error occurs due to rollback", throwables);
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
    public void delete(long id) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement(SQL_DELETE_JUNCTIONS);
                ps.setInt(1, (int) id);
                ps.executeUpdate();
                ps = connection.prepareStatement(SQL_DELETE_CERTIFICATE);
                ps.setInt(1, (int) id);
                ps.executeUpdate();
                connection.commit();
            } catch (SQLException throwables) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new DaoException("error occurs due to rollback", throwables);
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
    public Optional<GiftCertificate> find(long id) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement
                        (SQL_FIND_SPECIFIC_CERTIFICATE + SQL_SUFFIX_FOR_PAGINATION);
                ps.setInt(1, (int) id);
                ps.setInt(2, 1);
                ps.setInt(3, 0);
                ResultSet rs = ps.executeQuery();
                List<GiftCertificate> specificCertificates = createFoundList(rs);
                return specificCertificates.stream().findFirst();
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
    public List<GiftCertificate> findAll(int limit, int page) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement
                        (SQL_FIND_ALL_CERTIFICATES + SQL_SUFFIX_FOR_PAGINATION);
                ps.setInt(1, limit);
                ps.setInt(2, limit * (page - 1));
                ResultSet rs = ps.executeQuery();
                List<GiftCertificate> allCertificates = createFoundList(rs);
                return allCertificates;
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
    public void update(GiftCertificate certificateForUpdate, long id) throws DaoException {
        try {
            Connection connection = dataSource.getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement(SQL_FIND_SPECIFIC_CERTIFICATE);
                ps.setInt(1, (int) id);
                ResultSet rs = ps.executeQuery();
                List<GiftCertificate> certificates = createFoundList(rs);
                if (!certificates.isEmpty()) {
                    GiftCertificate certificateFromDb = certificates.get(0);
                    GiftCertificate forUpdate = addInfoForUpdate(certificateFromDb, certificateForUpdate);
                    ps = connection.prepareStatement(SQL_UPDATE_CERTIFICATE);
                    ps.setString(1, forUpdate.getName());
                    ps.setString(2, forUpdate.getDescription());
                    ps.setLong(3, forUpdate.getPrice());
                    ps.setDate(4, Date.valueOf(forUpdate.getDuration()
                            .addTo(LocalDate.now()).toString()));
                    ps.setInt(5, (int) id);
                    ps.executeUpdate();
                    connection.commit();
                }
            } catch (SQLException throwables) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new DaoException("error due to rollback", throwables);
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
    public List<GiftCertificate> sortCertificates(String field, String method, int limit, int page)
            throws DaoException {
        try {
            Connection connection = dataSource.getConnection();
            try {
                String specificRequest = createSpecificRequestForSort(field, method);
                PreparedStatement ps = connection.prepareStatement(specificRequest);
                ps.setInt(1, limit);
                ps.setInt(2, (page - 1) * limit);
                ResultSet rs = ps.executeQuery();
                return createFoundList(rs);
            } catch (SQLException throwables) {
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public List<GiftCertificate> searchByName(String name, int limit, int page) throws DaoException {
        return searchCertificatesByParam(name, SQL_SEARCH_BY_PART_OF_NAME, limit, page);
    }

    @Override
    public List<GiftCertificate> searchByDescription(String description, int limit, int page) throws DaoException {
        return searchCertificatesByParam(description, SQL_SEARCH_BY_PART_OF_DESCRIPTION, limit, page);
    }

    private List<GiftCertificate> searchCertificatesByParam(String param, String sqlSearchRequest, int limit, int page)
            throws DaoException {
        try {
            Connection connection = dataSource.getConnection();
            try {
                String specificRequest = createSpecificRequestForPartSearch(param, sqlSearchRequest);
                PreparedStatement ps = connection.prepareStatement(specificRequest);
                ps.setInt(1, limit);
                ps.setInt(2, limit * (page - 1));
                ResultSet rs = ps.executeQuery();
                return createFoundList(rs);
            } catch (SQLException throwables) {
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<GiftCertificate> searchByTag(String nameOfTag, int limit, int page) throws DaoException {
        try {
            Connection connection = dataSource.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_TAGS + SQL_SUFFIX_FOR_PAGINATION);
                ps.setString(1, nameOfTag);
                ps.setInt(2, limit);
                ps.setInt(3, limit * (page - 1));
                ResultSet rs = ps.executeQuery();
                return createFoundList(rs);
            } catch (SQLException throwables) {
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }

    }

    private List<GiftCertificate> createFoundList(ResultSet rs) throws SQLException {
        List<GiftCertificate> resultList = new ArrayList<>();
        boolean flag;
        while (rs.next()) {
            flag = false;
            long id = rs.getLong(SQL_COLUMN_ID_CERTIFICATE);
            String name = rs.getString(SQL_COLUMN_NAME_CERTIFICATE);
            String description = rs.getString(SQL_COLUMN_DESCRIPTION);
            long price = rs.getLong(SQL_COLUMN_PRICE);
            LocalDate duration = rs.getDate(SQL_COLUMN_DURATION).toLocalDate();
            LocalDateTime createDate = rs.getTimestamp(SQL_COLUMN_CREATE_DATE).toLocalDateTime();
            LocalDateTime lastUpdateDate = rs.getTimestamp(SQL_COLUMN_LAST_UPDATE_DATE).toLocalDateTime();
            long idTag = rs.getLong(SQL_COLUMN_ID_TAG);
            String nameOfTag = rs.getString(SQL_COLUMN_NAME_TAG);
            for (GiftCertificate existCertificate : resultList) {
                if (existCertificate.getId() == id) {
                    existCertificate.setTagsDependsOnCertificate(new Tag.TagBuilder().buildId(idTag)
                            .buildName(nameOfTag).finishBuilding());
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                LocalDate correctLocalDate = duration
                        .minusDays(LocalDate.now().getLong(ChronoField.DAY_OF_MONTH))
                        .minusMonths(LocalDate.now().getLong(ChronoField.MONTH_OF_YEAR))
                        .minusYears(LocalDate.now().getLong(ChronoField.YEAR));
                resultList.add(new GiftCertificate.GiftCertificateBuilder()
                        .buildId(id).buildName(name).buildDescription(description)
                        .buildPrice(price)
                        .buildDuration(Period.of(correctLocalDate.getYear(), correctLocalDate.getMonthValue(),
                                correctLocalDate.getDayOfMonth()).toString())
                        .buildCreateDate(createDate.toInstant(ZoneOffset.UTC).toString())
                        .buildLastUpdateDate(lastUpdateDate.toInstant(ZoneOffset.UTC).toString())
                        .buildTagDependsOnCertificate(new Tag.TagBuilder().buildId(idTag)
                                .buildName(nameOfTag).finishBuilding())
                        .finishBuilding());
            }

        }
        return resultList;
    }

    private String createSpecificRequestForSort(String field, String method) {
        StringBuilder sb = new StringBuilder(SQL_SORT_CERTIFICATES);
        sb.append(field);
        sb.append(" ");
        sb.append(method);
        sb.append(SQL_SUFFIX_FOR_PAGINATION);
        return sb.toString();
    }

    private String createSpecificRequestForPartSearch(String param, String baseRequest) {
        StringBuilder sb = new StringBuilder(baseRequest);
        sb.append("'%");
        sb.append(param);
        sb.append("%'");
        sb.append(SQL_SUFFIX_FOR_PAGINATION);
        return sb.toString();
    }

    private GiftCertificate addInfoForUpdate
            (GiftCertificate certificateFromDb, GiftCertificate certificateForUpdate) {
        if (certificateForUpdate.getName() == null) {
            certificateForUpdate.setName(certificateFromDb.getName());
        }
        if (certificateForUpdate.getDescription() == null) {
            certificateForUpdate.setDescription(certificateFromDb.getDescription());
        }
        if (certificateForUpdate.getPrice() == 0) {
            certificateForUpdate.setPrice(certificateFromDb.getPrice());
        }
        if (certificateForUpdate.getDuration() == null) {
            certificateForUpdate.setDuration(certificateFromDb.getDuration());
        }
        return certificateForUpdate;
    }

}
