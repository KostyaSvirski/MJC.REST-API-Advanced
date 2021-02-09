package com.epam.esm.entity;

import com.epam.esm.converter.TagDTOToTagEntityConverter;
import com.epam.esm.dto.TagDTO;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GiftCertificate {

    private long id;
    private String name;
    private String description;
    private long price;
    private Period duration;
    private Instant createDate;
    private Instant lastUpdateDate;
    private List<Tag> tagsDependsOnCertificate;

    public GiftCertificate(long id, String name, String description, long price, Period duration,
                           Instant createDate, Instant lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificate() {
    }

    public GiftCertificate(long id) {
        this.id = id;
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

    public Period getDuration() {
        return duration;
    }

    public void setDuration(Period duration) {
        this.duration = duration;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<Tag> getTagsDependsOnCertificate() {
        return tagsDependsOnCertificate;
    }

    public void setTagsDependsOnCertificate(Tag tag) {
        if (tagsDependsOnCertificate == null) {
            this.tagsDependsOnCertificate = new ArrayList<>();
        }
        this.tagsDependsOnCertificate.add(tag);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return price == that.price && Objects.equals(description, that.description)
                && Objects.equals(duration, that.duration)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate)
                && Objects.equals(tagsDependsOnCertificate, that.tagsDependsOnCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, price, duration, createDate, lastUpdateDate,
                tagsDependsOnCertificate);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GiftCertificate{");
        sb.append("description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", duration=").append(duration);
        sb.append(", createDate=").append(createDate);
        sb.append(", lastUpdateDate=").append(lastUpdateDate);
        sb.append(", tagDependsOnCertificate=").append(tagsDependsOnCertificate);
        sb.append('}');
        return sb.toString();
    }

    public static class GiftCertificateBuilder {

        private GiftCertificate newGiftCertificate;

        public GiftCertificateBuilder() {
            newGiftCertificate = new GiftCertificate();
        }

        public GiftCertificateBuilder buildId(long id) {
            newGiftCertificate.setId(id);
            return this;
        }

        public GiftCertificateBuilder buildName(String name) {
            newGiftCertificate.setName(name);
            return this;
        }

        public GiftCertificateBuilder buildDescription(String description) {
            newGiftCertificate.setDescription(description);
            return this;

        }

        public GiftCertificateBuilder buildPrice(long price) {
            newGiftCertificate.setPrice(price);
            return this;
        }

        public GiftCertificateBuilder buildDuration(String duration) {
            if (duration != null) {
                newGiftCertificate.setDuration(Period.parse(duration));
            }
            return this;
        }

        public GiftCertificateBuilder buildCreateDate(String createDate) {
            if (createDate != null) {
                newGiftCertificate.setCreateDate(Instant.parse(createDate));
            }
            return this;
        }

        public GiftCertificateBuilder buildLastUpdateDate(String lastUpdateDate) {
            if (lastUpdateDate != null) {
                newGiftCertificate.setLastUpdateDate(Instant.parse(lastUpdateDate));
            }
            return this;
        }

        public GiftCertificateBuilder buildTagDependsOnCertificate(Tag tagDependsOnCertificate) {
            newGiftCertificate.setTagsDependsOnCertificate(tagDependsOnCertificate);
            return this;
        }

        public GiftCertificateBuilder buildListOfTags(List<TagDTO> tags) {
            if (tags != null) {
                newGiftCertificate.tagsDependsOnCertificate = tags.stream()
                        .map(tag -> new TagDTOToTagEntityConverter().apply(tag))
                        .collect(Collectors.toList());
            }
            return this;
        }

        public GiftCertificate finishBuilding() {
            return newGiftCertificate;
        }

    }

}
