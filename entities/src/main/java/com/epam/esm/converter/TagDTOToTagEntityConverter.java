package com.epam.esm.converter;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TagDTOToTagEntityConverter implements Function<TagDTO, Tag> {

    @Override
    public Tag apply(TagDTO tagDTO) {
        return new Tag.TagBuilder().buildName(tagDTO.getName()).buildId(tagDTO.getId()).finishBuilding();
    }
}
