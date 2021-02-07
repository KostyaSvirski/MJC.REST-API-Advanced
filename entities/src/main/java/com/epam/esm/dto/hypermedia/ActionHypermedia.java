package com.epam.esm.dto.hypermedia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

public class ActionHypermedia extends RepresentationModel<ActionHypermedia> {

    private final String message;

    @JsonCreator
    public ActionHypermedia(@JsonProperty("content") String content) {
        this.message = content;
    }

    public String getMessage() {
        return message;
    }
}
