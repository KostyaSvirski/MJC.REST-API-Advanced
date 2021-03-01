package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

public class CreateActionHypermedia extends RepresentationModel<CreateActionHypermedia> {

    private final int id;

    public CreateActionHypermedia(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
