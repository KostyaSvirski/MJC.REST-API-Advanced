package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDTOToEntityConverter implements Function<GiftCertificateDTO, GiftCertificateEntity> {

    @Override
    public GiftCertificateEntity apply(GiftCertificateDTO certificateDTO) {
        return GiftCertificateEntity.builder().id(certificateDTO.getId())
                .name(certificateDTO.getName())
                .createDate(certificateDTO.getCreateDate() != null ? LocalDate.parse(certificateDTO.getCreateDate()) : null)
                .lastUpdateDate(LocalDateTime.parse(certificateDTO.getLastUpdateDate()))
                .description(certificateDTO.getDescription())
                .duration(certificateDTO.getDuration())
                .price(certificateDTO.getPrice())
                .tagsDependsOnCertificate(certificateDTO.getTags().stream()
                        .map(tag -> new TagDTOToTagEntityConverter().apply(tag)).collect(Collectors.toSet())).build();
    }
}
