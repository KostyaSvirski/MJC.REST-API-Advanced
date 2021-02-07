package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class GiftCertificateDTOToEntityConverter implements Function<GiftCertificateDTO, GiftCertificate> {

    @Override
    public GiftCertificate apply(GiftCertificateDTO certificateDTO) {
        return new GiftCertificate.GiftCertificateBuilder().buildName(certificateDTO.getName())
                .buildDescription(certificateDTO.getDescription())
                .buildPrice(certificateDTO.getPrice())
                .buildCreateDate(certificateDTO.getCreateDate())
                .buildDuration(certificateDTO.getDuration())
                .buildLastUpdateDate(certificateDTO.getLastUpdateDate())
                .buildListOfTags(certificateDTO.getTags())
                .finishBuilding();
    }
}
