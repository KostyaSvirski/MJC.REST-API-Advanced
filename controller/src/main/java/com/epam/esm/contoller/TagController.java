package com.epam.esm.contoller;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.builder.ActionHypermediaLinkBuilder;
import com.epam.esm.util.builder.CreateHypermediaLinkBuilder;
import com.epam.esm.util.builder.TagLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public ResponseEntity<?> findAllTags(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                         @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        try {
            List<TagDTO> allTags = tagService.findAll(limit, page);
            for (int i = 0; i < allTags.size(); i++) {
                TagLinkBuilder builder = new TagLinkBuilder(allTags.get(i));
                builder.buildCertificatesDependsOnTagLink();
                allTags.set(i, builder.getHypermedia());
            }
            return new ResponseEntity<>(allTags, HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(e.getMessage()));
            builder.buildFindAllTagsSelfLink(limit, page);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSpecificTag(@PathVariable long id) {
        try {
            Optional<TagDTO> tagToFind = tagService.find(id);
            if (tagToFind.isPresent()) {
                TagLinkBuilder builder = new TagLinkBuilder(tagToFind.get());
                builder.buildCertificatesDependsOnTagLink();
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
            } else {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                        (new ActionHypermedia("not found"));
                builder.buildFindAllTagsSelfLink(Integer.parseInt(DEFAULT_LIMIT), Integer.parseInt(DEFAULT_PAGE));
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                    (new ActionHypermedia(e.getMessage()));
            builder.buildFindSpecificTagSelfLink(id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/")
    public ResponseEntity<?> addTag(@RequestBody TagDTO newTag) {
        try {
            int result = tagService.create(newTag);
            if (result != 0) {
                CreateHypermediaLinkBuilder builder = new CreateHypermediaLinkBuilder(new CreateActionHypermedia(result));
                builder.buildNewTagLink(result);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.CREATED);
            } else {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                        (new ActionHypermedia("not valid data"));
                builder.buildAddTagSelfLink(newTag);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                    (new ActionHypermedia(e.getMessage()));
            builder.buildAddTagSelfLink(newTag);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable long id) {
        try {
            tagService.delete(id);
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                    (new ActionHypermedia("tag with id " + id + " deleted"));
            builder.buildFindAllTagsLink(Integer.parseInt(DEFAULT_LIMIT), Integer.parseInt(DEFAULT_PAGE));
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                    (new ActionHypermedia(e.getMessage()));
            builder.buildDeleteTagSelfLink(id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
