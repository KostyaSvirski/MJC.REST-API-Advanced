package com.epam.esm.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"certificateEntitySet"})
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
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "tagsDependsOnCertificate")
    @Getter
    @Setter
    private Set<HibernateGiftCertificateEntity> certificateEntitySet = new HashSet<>();

    public void addCertificate(HibernateGiftCertificateEntity cert) {
        if(!cert.getTagsDependsOnCertificate().contains(this)) {
            cert.addTag(this);
        }
        certificateEntitySet.add(cert);
    }

    public void removeCertificate(HibernateGiftCertificateEntity cert) {
        certificateEntitySet.remove(cert);
        cert.getTagsDependsOnCertificate().remove(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HibernateTagEntity{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
