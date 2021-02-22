package com.epam.esm.converter;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.persistence.TagEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TagDTOToTagEntityConverter implements Function<TagDTO, TagEntity> {

    @Override
    public TagEntity apply(TagDTO tagDTO) {
        return TagEntity.builder().id(tagDTO.getId()).name(tagDTO.getName()).build();
    }
}
