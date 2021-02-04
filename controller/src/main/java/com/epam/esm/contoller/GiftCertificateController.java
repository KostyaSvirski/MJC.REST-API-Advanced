package com.epam.esm.contoller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.hypermedia.CreateActionHypermedia;
import com.epam.esm.dto.hypermedia.ActionHypermedia;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";

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
                ActionHypermedia actionHypermedia = new ActionHypermedia("no content");
                actionHypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                                .retrieveCertificates(limit, page, partOfName, partOfDescription, nameOfTag, field,
                                        method))
                        .withSelfRel());
                return new ResponseEntity<>(actionHypermedia, HttpStatus.NOT_FOUND);
            }
            for(GiftCertificateDTO dto : results) {
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                                .findSpecificCertificate(dto.getId()))
                        .withRel("certificate"));
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .retrieveCertificates(limit, page, partOfName, partOfDescription, nameOfTag, field,
                                    method))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSpecificCertificate(@PathVariable long id) {
        Optional<GiftCertificateDTO> result;
        try {
            result = service.find(id,1, 1);
            return result
                    .map(certificateDTO -> new ResponseEntity<>(certificateDTO, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity(new ActionHypermedia
                            ("certificate with id " + id + "not found"),
                            HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .findSpecificCertificate(id))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewCertificate(@RequestBody GiftCertificateDTO certificateDTO) {
        certificateDTO.setCreateDate(Instant.now().toString());
        certificateDTO.setLastUpdateDate(Instant.now().toString());
        try {
            int result = service.create(certificateDTO);
            if (result != 0) {
                CreateActionHypermedia hypermedia = new CreateActionHypermedia(result);
                hypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                                .findSpecificCertificate(result))
                        .withRel("certificate"));
                return new ResponseEntity<>(hypermedia, HttpStatus.CREATED);
            } else {
                ActionHypermedia actionHypermedia = new ActionHypermedia("not valid data");
                actionHypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                                .createNewCertificate(certificateDTO))
                        .withSelfRel());
                return new ResponseEntity<>(actionHypermedia, HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .createNewCertificate(certificateDTO))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(new ActionHypermedia("certificate with id " + id + " deleted"),
                    HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .deleteCertificate(id))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.NOT_FOUND);
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificateDTO certificate, @PathVariable long id) {
        try {
            service.update(certificate, id);
            ActionHypermedia actionHypermedia = new ActionHypermedia("updated with id" + id);
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .findSpecificCertificate(id))
                    .withRel("certificate"));
            return new ResponseEntity<>(actionHypermedia, HttpStatus.NO_CONTENT);
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .updateCertificate(certificate, id))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.NOT_FOUND);
        }

    }


}
