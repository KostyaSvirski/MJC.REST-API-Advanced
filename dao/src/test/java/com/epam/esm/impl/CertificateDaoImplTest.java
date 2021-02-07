package com.epam.esm.impl;

import com.epam.esm.GiftCertificateDao;
import com.epam.esm.config.ConfigDB;
import com.epam.esm.exception.DaoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = ConfigDB.class)
@ExtendWith(SpringExtension.class)
class CertificateDaoImplTest {

    @Autowired
    private GiftCertificateDao dao;

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 5, 6, 9})
    public void testFindSpecific(long id) throws DaoException {
        assertEquals(id, dao.find(id, 1, 1).get(0).getId());
    }

}