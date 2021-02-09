package com.epam.esm.converter;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;

import java.util.function.Function;

public class TagDTOToTagEntityConverter implements Function<TagDTO, Tag> {

    @Override
    public Tag apply(TagDTO tagDTO) {
        return new Tag.TagBuilder().buildName(tagDTO.getName()).buildId(tagDTO.getId()).finishBuilding();
    }
}
