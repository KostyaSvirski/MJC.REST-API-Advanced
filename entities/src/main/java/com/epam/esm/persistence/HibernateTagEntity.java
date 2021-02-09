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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tag_for_certificates")
public class HibernateTagEntity {

    @Column(name = "id_tag")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;
    @Column(name = "name_of_tag")
    @Getter
    @Setter
    private String name;
    @ManyToMany(mappedBy = "tagsDependsOnCertificate")
    @Getter
    @Setter
    private Set<HibernateGiftCertificateEntity> certificateEntitySet;
}
