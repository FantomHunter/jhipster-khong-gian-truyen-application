package com.codehunter.khonggiantruyen.web.rest;

import com.codehunter.khonggiantruyen.domain.Comment;
import com.codehunter.khonggiantruyen.domain.Product;
import com.codehunter.khonggiantruyen.repository.CommentExtendsRepository;
import com.codehunter.khonggiantruyen.repository.CommentRepository;
import com.codehunter.khonggiantruyen.repository.ProductRepository;
import com.codehunter.khonggiantruyen.security.SecurityUtils;
import com.codehunter.khonggiantruyen.service.UserService;
import com.codehunter.khonggiantruyen.service.dto.CommentExtendsDTO;
import com.codehunter.khonggiantruyen.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
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
    private final UserService userService;
    private final ProductRepository productRepository;
    private final CommentResource commentResource;

    public CommentExtendsResource(
        CommentExtendsRepository commentExtendsRepository,
        UserService userService,
        ProductRepository productRepository,
        CommentResource commentResource
    ) {
        this.commentExtendsRepository = commentExtendsRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.commentResource = commentResource;
    }

    @GetMapping("/comments/product/{id}")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get a page of Comments by product id {}", id);
        Page<Comment> page = commentExtendsRepository.findAllWithEagerRelationships(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/comments/extends")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentExtendsDTO commentExtendsDTO) throws URISyntaxException {
        log.debug("REST request to save Comment by user : {}", commentExtendsDTO);
        Comment comment = new Comment();
        comment.setContent(commentExtendsDTO.getContent());
        comment.setCommentDate(Instant.now());
        comment.setProduct(
            productRepository
                .findById(commentExtendsDTO.getProductId())
                .orElseThrow(() -> new BadRequestAlertException("Entity not found", "CommentExtendsDTO", "idnotfound"))
        );
        comment.setUser(
            userService
                .getUserWithAuthorities()
                .orElseThrow(() -> new BadRequestAlertException("User not found", "CommentExtendsDTO", "idnotfound"))
        );
        return commentResource.createComment(comment);
    }
}
