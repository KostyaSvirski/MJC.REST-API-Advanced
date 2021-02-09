package com.epam.esm.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gift_certificate")
public class HibernateGiftCertificateEntity {

    @Column(name = "id_certificate")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;
    @Column(name = "name_of_certificate")
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private long price;
    @Getter
    @Setter
    private Date duration;
    @Column(name = "create_date")
    @Getter
    @Setter
    private Date createDate;
    @Column(name = "last_update_date")
    @Getter
    @Setter
    private Timestamp lastUpdateDate;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "junction_gift_certificates_and_tags",
            joinColumns = {@JoinColumn(name = "id_certificate")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")}
    )
    @Getter
    @Setter
    private Set<HibernateTagEntity> tagsDependsOnCertificate;


}
