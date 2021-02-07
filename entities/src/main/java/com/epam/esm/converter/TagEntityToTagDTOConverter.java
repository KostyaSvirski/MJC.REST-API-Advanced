package com.epam.esm.converter;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TagEntityToTagDTOConverter implements Function<Tag, TagDTO> {

    @Override
    public TagDTO apply(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
