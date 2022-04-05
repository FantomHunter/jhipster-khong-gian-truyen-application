package com.codehunter.khonggiantruyen.web.rest;

import com.codehunter.khonggiantruyen.domain.Product;
import com.codehunter.khonggiantruyen.domain.ProductWithLatestCommentDate;
import com.codehunter.khonggiantruyen.repository.ProductExtendsRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductExtendsResource {

    private final Logger log = LoggerFactory.getLogger(ProductExtendsResource.class);
    private final ProductExtendsRepository productExtendsRepository;

    public ProductExtendsResource(ProductExtendsRepository productExtendsRepository) {
        this.productExtendsRepository = productExtendsRepository;
    }

    @GetMapping("/products/new-comment")
    public ResponseEntity<List<Product>> getTopNewCommentedProducts(Pageable pageable) {
        log.debug("REST request to get a page of Products");
        Page<ProductWithLatestCommentDate> page;
        page = productExtendsRepository.findAllNewsCommentedProduct(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(page.getContent().stream().map(ProductWithLatestCommentDate::getProduct).collect(Collectors.toList()));
    }

    @GetMapping("/products/comments")
    public ResponseEntity<List<Product>> findAllProductWithComment(Pageable pageable) {
        log.debug("REST request to get a page of Products");

        Page<Long> page;
        page = productExtendsRepository.getProductIds(pageable);
        List<Product> allProducts = productExtendsRepository.findAllProducts(page.getContent(), pageable.getSort());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(allProducts);
    }
}
