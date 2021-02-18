package com.epam.esm.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = "certificateEntitySet")
@ToString(exclude = "certificateEntitySet")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tag_for_certificates")
public class TagEntity {

    @Column(name = "id_tag")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name_of_tag")
    private String name;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,
                    CascadeType.DETACH,
                    CascadeType.REFRESH,
                    CascadeType.REMOVE}, mappedBy = "tagsDependsOnCertificate")
    private Set<GiftCertificateEntity> certificateEntitySet = new HashSet<>();

    public void addCertificate(GiftCertificateEntity cert) {
        if (!cert.getTagsDependsOnCertificate().contains(this)) {
            cert.addTag(this);
        }
        certificateEntitySet.add(cert);
    }

    public void removeCertificate(GiftCertificateEntity cert) {
        certificateEntitySet.remove(cert);
        cert.getTagsDependsOnCertificate().remove(this);
    }

}
