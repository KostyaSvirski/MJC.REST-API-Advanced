package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

@Component
public class TagDTO extends RepresentationModel<TagDTO> {

    private String name;
    private long id;

    public TagDTO(long id, String name) {
        this.name = name;
        this.id = id;
    }

    public TagDTO() {
    }

    public TagDTO(long id) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TagDTO{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
