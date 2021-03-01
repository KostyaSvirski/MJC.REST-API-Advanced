package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GiftCertificateEntityToDTOConverter implements Function<GiftCertificateEntity, GiftCertificateDTO> {

    @Override
    public GiftCertificateDTO apply(GiftCertificateEntity giftCertificate) {
        return GiftCertificateDTO.builder().id(giftCertificate.getId()).duration(giftCertificate.getDuration())
                .createDate(giftCertificate.getCreateDate().toString()).description(giftCertificate.getDescription())
                .name(giftCertificate.getName()).price(giftCertificate.getPrice())
                .lastUpdateDate(giftCertificate.getLastUpdateDate().toString())
                .tags(giftCertificate.getTagsDependsOnCertificate().stream()
                        .map(tag -> new TagEntityToTagDTOConverter().apply(tag)).collect(Collectors.toSet())).build();
    }
}
