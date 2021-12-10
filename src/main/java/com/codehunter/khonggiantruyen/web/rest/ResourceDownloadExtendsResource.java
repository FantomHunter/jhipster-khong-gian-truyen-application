package com.codehunter.khonggiantruyen.web.rest;

import com.codehunter.khonggiantruyen.domain.ResourceDownload;
import com.codehunter.khonggiantruyen.repository.ResourceDownloadRepository;
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

@RestController
@RequestMapping("/api")
@Transactional
public class ResourceDownloadExtendsResource {

    private final Logger log = LoggerFactory.getLogger(ResourceDownloadExtendsResource.class);

    private final ResourceDownloadRepository resourceDownloadRepository;

    public ResourceDownloadExtendsResource(ResourceDownloadRepository resourceDownloadRepository) {
        this.resourceDownloadRepository = resourceDownloadRepository;
    }

    /**
     * {@code GET  /resource-downloads/product/:id} : get all the resourceDownloads by a product id.
     *
     * @param id the product id
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceDownloads in body.
     */
    @GetMapping("/resource-downloads/product/{id}")
    public ResponseEntity<List<ResourceDownload>> getAllResourceDownloads(@PathVariable Long id) {
        log.debug("REST request to get ResourceDownloads of product");
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findResourceDownloadByProduct(id);
        return ResponseEntity.ok().body(resourceDownloadList);
    }
}
