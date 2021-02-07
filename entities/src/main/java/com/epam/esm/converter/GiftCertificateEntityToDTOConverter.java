package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GiftCertificateEntityToDTOConverter implements Function<GiftCertificate, GiftCertificateDTO> {

    @Override
    public GiftCertificateDTO apply(GiftCertificate giftCertificate) {
        GiftCertificateDTO dto = new GiftCertificateDTO();
        dto.setId(giftCertificate.getId());
        dto.setName(giftCertificate.getName());
        dto.setDescription(giftCertificate.getDescription());
        dto.setPrice(giftCertificate.getPrice());
        dto.setCreateDate(giftCertificate.getCreateDate().toString());
        dto.setDuration(giftCertificate.getDuration().toString());
        dto.setLastUpdateDate(giftCertificate.getLastUpdateDate().toString());
        dto.setTags(giftCertificate.getTagsDependsOnCertificate().stream()
                .map(tag -> new TagEntityToTagDTOConverter().apply(tag)).collect(Collectors.toList()));
        return dto;
    }
}
