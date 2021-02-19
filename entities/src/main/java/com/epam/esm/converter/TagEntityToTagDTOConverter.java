package com.epam.esm.converter;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.persistence.TagEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TagEntityToTagDTOConverter implements Function<TagEntity, TagDTO> {

    @Override
    public TagDTO apply(TagEntity tag) {
        return TagDTO.builder().name(tag.getName()).id(tag.getId()).build();
    }
}
