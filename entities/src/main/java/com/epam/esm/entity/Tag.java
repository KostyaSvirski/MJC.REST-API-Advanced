package com.epam.esm.entity;

import java.util.List;
import java.util.Objects;

public class Tag {

    private long id;
    private String name;

    public Tag() {
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(long id) {
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
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(name, tag.name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static class TagBuilder {
        private Tag newTag;

        public TagBuilder() {
            this.newTag = new Tag();
        }

        public TagBuilder buildId(long id) {
            newTag.setId(id);
            return this;
        }

        public TagBuilder buildName(String name) {
            newTag.setName(name);
            return this;
        }

        public Tag finishBuilding() {
            return newTag;
        }
    }
}
