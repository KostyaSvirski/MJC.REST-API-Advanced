package com.epam.esm.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftCertificateDTO extends RepresentationModel<GiftCertificateDTO> {

    private long id;
    private String name;
    private String description;
    private long price;
    private String duration;
    private String createDate;
    private String lastUpdateDate;
    private List<TagDTO> tags;

    public GiftCertificateDTO(long id, String name, String description, long price,
                              String duration, String createDate, String lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificateDTO() {
    }

    public GiftCertificateDTO(long id, String name, String description, long price,
                              String duration, String createDate, String lastUpdateDate, List<TagDTO> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificateDTO(long id, String name, long price, String duration, String createDate,
                              String lastUpdateDate, List<TagDTO> tags) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificateDTO(String name, long price, String duration, List<TagDTO> tags) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
    }

    public GiftCertificateDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    @Autowired
    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        String sb = "GiftCertificateDTO{" + "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
        return sb;
    }
}
