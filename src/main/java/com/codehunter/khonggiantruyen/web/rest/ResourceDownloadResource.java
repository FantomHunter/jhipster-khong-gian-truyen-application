package com.codehunter.khonggiantruyen.web.rest;

import com.codehunter.khonggiantruyen.domain.ResourceDownload;
import com.codehunter.khonggiantruyen.repository.ResourceDownloadRepository;
import com.codehunter.khonggiantruyen.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.codehunter.khonggiantruyen.domain.ResourceDownload}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResourceDownloadResource {

    private final Logger log = LoggerFactory.getLogger(ResourceDownloadResource.class);

    private static final String ENTITY_NAME = "resourceDownload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceDownloadRepository resourceDownloadRepository;

    public ResourceDownloadResource(ResourceDownloadRepository resourceDownloadRepository) {
        this.resourceDownloadRepository = resourceDownloadRepository;
    }

    /**
     * {@code POST  /resource-downloads} : Create a new resourceDownload.
     *
     * @param resourceDownload the resourceDownload to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceDownload, or with status {@code 400 (Bad Request)} if the resourceDownload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-downloads")
    public ResponseEntity<ResourceDownload> createResourceDownload(@Valid @RequestBody ResourceDownload resourceDownload)
        throws URISyntaxException {
        log.debug("REST request to save ResourceDownload : {}", resourceDownload);
        if (resourceDownload.getId() != null) {
            throw new BadRequestAlertException("A new resourceDownload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceDownload result = resourceDownloadRepository.save(resourceDownload);
        return ResponseEntity
            .created(new URI("/api/resource-downloads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-downloads/:id} : Updates an existing resourceDownload.
     *
     * @param id the id of the resourceDownload to save.
     * @param resourceDownload the resourceDownload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceDownload,
     * or with status {@code 400 (Bad Request)} if the resourceDownload is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceDownload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-downloads/{id}")
    public ResponseEntity<ResourceDownload> updateResourceDownload(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceDownload resourceDownload
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceDownload : {}, {}", id, resourceDownload);
        if (resourceDownload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceDownload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceDownloadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResourceDownload result = resourceDownloadRepository.save(resourceDownload);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceDownload.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resource-downloads/:id} : Partial updates given fields of an existing resourceDownload, field will ignore if it is null
     *
     * @param id the id of the resourceDownload to save.
     * @param resourceDownload the resourceDownload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceDownload,
     * or with status {@code 400 (Bad Request)} if the resourceDownload is not valid,
     * or with status {@code 404 (Not Found)} if the resourceDownload is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceDownload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resource-downloads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResourceDownload> partialUpdateResourceDownload(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceDownload resourceDownload
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceDownload partially : {}, {}", id, resourceDownload);
        if (resourceDownload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceDownload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceDownloadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourceDownload> result = resourceDownloadRepository
            .findById(resourceDownload.getId())
            .map(existingResourceDownload -> {
                if (resourceDownload.getUrl() != null) {
                    existingResourceDownload.setUrl(resourceDownload.getUrl());
                }
                if (resourceDownload.getFormat() != null) {
                    existingResourceDownload.setFormat(resourceDownload.getFormat());
                }

                return existingResourceDownload;
            })
            .map(resourceDownloadRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceDownload.getId().toString())
        );
    }

    /**
     * {@code GET  /resource-downloads} : get all the resourceDownloads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceDownloads in body.
     */
    @GetMapping("/resource-downloads")
    public ResponseEntity<List<ResourceDownload>> getAllResourceDownloads(Pageable pageable) {
        log.debug("REST request to get a page of ResourceDownloads");
        Page<ResourceDownload> page = resourceDownloadRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resource-downloads/:id} : get the "id" resourceDownload.
     *
     * @param id the id of the resourceDownload to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceDownload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-downloads/{id}")
    public ResponseEntity<ResourceDownload> getResourceDownload(@PathVariable Long id) {
        log.debug("REST request to get ResourceDownload : {}", id);
        Optional<ResourceDownload> resourceDownload = resourceDownloadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resourceDownload);
    }

    /**
     * {@code DELETE  /resource-downloads/:id} : delete the "id" resourceDownload.
     *
     * @param id the id of the resourceDownload to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-downloads/{id}")
    public ResponseEntity<Void> deleteResourceDownload(@PathVariable Long id) {
        log.debug("REST request to delete ResourceDownload : {}", id);
        resourceDownloadRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
