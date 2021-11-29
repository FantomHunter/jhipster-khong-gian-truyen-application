package com.codehunter.khonggiantruyen.web.rest;

import com.codehunter.khonggiantruyen.domain.Comment;
import com.codehunter.khonggiantruyen.repository.CommentExtendsRepository;
import com.codehunter.khonggiantruyen.repository.CommentRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * @author codehunter
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommentExtendsResource {

    private final Logger log = LoggerFactory.getLogger(CommentExtendsResource.class);
    private final CommentExtendsRepository commentExtendsRepository;
    private final CommentResource commentResource;

    public CommentExtendsResource(CommentExtendsRepository commentExtendsRepository, CommentResource commentResource) {
        this.commentExtendsRepository = commentExtendsRepository;
        this.commentResource = commentResource;
    }

    @GetMapping("/comments/product/{id}")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get a page of Comments by product id {}", id);
        Page<Comment> page = commentExtendsRepository.findAllWithEagerRelationships(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
