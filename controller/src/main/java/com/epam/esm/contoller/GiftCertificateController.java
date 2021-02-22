package com.epam.esm.contoller;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.builder.ActionHypermediaLinkBuilder;
import com.epam.esm.util.builder.CertificateLinkBuilder;
import com.epam.esm.util.builder.CreateHypermediaLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";
    private static final String ERROR_MESSAGE = "error";

    @Autowired
    private GiftCertificateService service;

    @GetMapping(value = "/")
    public ResponseEntity<?> retrieveCertificates
            (@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
             @RequestParam(defaultValue = DEFAULT_PAGE) int page,
             @RequestParam(required = false) String partOfName,
             @RequestParam(required = false) String partOfDescription,
             @RequestParam(required = false) String nameOfTag,
             @RequestParam(required = false) String field,
             @RequestParam(required = false) String method) {
        List<GiftCertificateDTO> results;
        try {
            if (partOfName != null) {
                results = service.findByPartOfName(partOfName, limit, page);
            } else if (partOfDescription != null) {
                results = service.findByPartOfDescription(partOfDescription, limit, page);
            } else if (nameOfTag != null) {
                results = service.findByTag(nameOfTag, limit, page);
            } else if (field != null && method != null) {
                results = service.sortByField(field, method, limit, page);
            } else {
                results = service.findAll(limit, page);
            }
            if (results == null || results.isEmpty()) {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia("not found"));
                builder.buildRetrieveAllCertificateSelfLink(limit, page, partOfName, partOfDescription, nameOfTag, field, method);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
            }
            for (int i = 0; i < results.size(); i++) {
                CertificateLinkBuilder builder = new CertificateLinkBuilder(results.get(i));
                builder.buildCertificateHypermedia();
                results.set(i, builder.getHypermedia());
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Throwable e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildRetrieveAllCertificateSelfLink(limit, page, partOfName, partOfDescription, nameOfTag,
                    field, method);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSpecificCertificate(@PathVariable long id) {
        Optional<GiftCertificateDTO> result;
        try {
            result = service.find(id);
            return result.map(certificateDTO -> new ResponseEntity<>(certificateDTO, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity(new ActionHypermedia("certificate with id " + id + "not found"),
                            HttpStatus.NOT_FOUND));
        } catch (Throwable e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildRetrieveSpecificCertificateSelfLink(id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewCertificate(@RequestBody GiftCertificateDTO certificateDTO) {
        certificateDTO.setCreateDate(Instant.now().toString());
        certificateDTO.setLastUpdateDate(Instant.now().toString());
        try {
            int result = service.create(certificateDTO);
            if (result != 0) {
                CreateHypermediaLinkBuilder builder = new CreateHypermediaLinkBuilder(new CreateActionHypermedia(result));
                builder.buildNewCertificateLink(result);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.CREATED);
            } else {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                        (new ActionHypermedia("not valid data"));
                builder.buildCreateCertificateSelfLink(certificateDTO);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.BAD_REQUEST);
            }
        } catch (Throwable e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildCreateCertificateSelfLink(certificateDTO);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(new ActionHypermedia("certificate with id " + id + " deleted"),
                    HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(e.getMessage()));
            builder.buildDeleteCertificateSelfLink(id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
        } catch (Throwable e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildDeleteCertificateSelfLink(id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificateDTO certificate, @PathVariable long id) {
        try {
            boolean result = service.update(certificate, id);
            if (result) {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                        (new ActionHypermedia("updated with id" + id));
                builder.buildRetrieveSpecificCertificateLink(id);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NO_CONTENT);
            } else {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                        (new ActionHypermedia("not valid data"));
                builder.buildUpdateCertificateSelfLink(certificate, id);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(e.getMessage()));
            builder.buildUpdateCertificateSelfLink(certificate, id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
        } catch (Throwable e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildUpdateCertificateSelfLink(certificate, id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
