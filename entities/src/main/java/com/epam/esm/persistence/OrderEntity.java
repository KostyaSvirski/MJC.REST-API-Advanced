package com.epam.esm.persistence;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_of_user")
public class OrderEntity {

    @Column(name = "id_order")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "id_user")
    private long idUser;
    @Column(name = "id_certificate")
    private long idCertificate;
    private long cost;
    private boolean closed;
    @Column(name = "purchase_time")
    private Timestamp purchaseTime;

}
