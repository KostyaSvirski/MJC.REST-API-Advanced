package com.epam.esm.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_of_user")
public class HibernateOrderEntity {

    @Column(name = "id_order")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    @Column(name = "id_user")
    private long idUser;
    @Column(name = "id_certificate")
    @Getter
    @Setter
    private long idCertificate;
    @Getter
    @Setter
    private long cost;
    @Getter
    @Setter
    private boolean closed;

}
