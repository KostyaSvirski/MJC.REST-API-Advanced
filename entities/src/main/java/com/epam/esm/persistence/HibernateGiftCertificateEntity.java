package com.epam.esm.persistence;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"tagsDependsOnCertificate"})
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
    private int duration;
    @Column(name = "create_date")
    @Getter
    @Setter
    private Date createDate;
    @Column(name = "last_update_date")
    @Getter
    @Setter
    private Timestamp lastUpdateDate;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE})
    @JoinTable(
            name = "junction_gift_certificates_and_tags",
            joinColumns = {@JoinColumn(name = "id_certificate")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")}
    )
    @Getter
    @Setter
    private Set<HibernateTagEntity> tagsDependsOnCertificate = new HashSet<>();

    public void addTag(HibernateTagEntity tag) {
        tagsDependsOnCertificate.add(tag);
        if (!tag.getCertificateEntitySet().contains(this)) {
            tag.addCertificate(this);
        }
    }

    public void removeTag(HibernateTagEntity tag) {
        tagsDependsOnCertificate.remove(tag);
        tag.getCertificateEntitySet().remove(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HibernateGiftCertificateEntity{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", duration=").append(duration);
        sb.append(", createDate=").append(createDate);
        sb.append(", lastUpdateDate=").append(lastUpdateDate);
        sb.append('}');
        return sb.toString();
    }
}
