package com.codehunter.khonggiantruyen.web.rest;

import com.codehunter.khonggiantruyen.domain.Download;
import com.codehunter.khonggiantruyen.repository.DownloadRepository;
import com.codehunter.khonggiantruyen.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.codehunter.khonggiantruyen.domain.Download}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DownloadResource {

    private final Logger log = LoggerFactory.getLogger(DownloadResource.class);

    private static final String ENTITY_NAME = "download";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DownloadRepository downloadRepository;

    public DownloadResource(DownloadRepository downloadRepository) {
        this.downloadRepository = downloadRepository;
    }

    /**
     * {@code POST  /downloads} : Create a new download.
     *
     * @param download the download to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new download, or with status {@code 400 (Bad Request)} if the download has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/downloads")
    public ResponseEntity<Download> createDownload(@RequestBody Download download) throws URISyntaxException {
        log.debug("REST request to save Download : {}", download);
        if (download.getId() != null) {
            throw new BadRequestAlertException("A new download cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Download result = downloadRepository.save(download);
        return ResponseEntity
            .created(new URI("/api/downloads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /downloads/:id} : Updates an existing download.
     *
     * @param id the id of the download to save.
     * @param download the download to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated download,
     * or with status {@code 400 (Bad Request)} if the download is not valid,
     * or with status {@code 500 (Internal Server Error)} if the download couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/downloads/{id}")
    public ResponseEntity<Download> updateDownload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Download download
    ) throws URISyntaxException {
        log.debug("REST request to update Download : {}, {}", id, download);
        if (download.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, download.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!downloadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Download result = downloadRepository.save(download);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, download.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /downloads/:id} : Partial updates given fields of an existing download, field will ignore if it is null
     *
     * @param id the id of the download to save.
     * @param download the download to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated download,
     * or with status {@code 400 (Bad Request)} if the download is not valid,
     * or with status {@code 404 (Not Found)} if the download is not found,
     * or with status {@code 500 (Internal Server Error)} if the download couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/downloads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Download> partialUpdateDownload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Download download
    ) throws URISyntaxException {
        log.debug("REST request to partial update Download partially : {}, {}", id, download);
        if (download.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, download.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!downloadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Download> result = downloadRepository
            .findById(download.getId())
            .map(existingDownload -> {
                if (download.getDownloadDate() != null) {
                    existingDownload.setDownloadDate(download.getDownloadDate());
                }

                return existingDownload;
            })
            .map(downloadRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, download.getId().toString())
        );
    }

    /**
     * {@code GET  /downloads} : get all the downloads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of downloads in body.
     */
    @GetMapping("/downloads")
    public List<Download> getAllDownloads() {
        log.debug("REST request to get all Downloads");
        return downloadRepository.findAll();
    }

    /**
     * {@code GET  /downloads/:id} : get the "id" download.
     *
     * @param id the id of the download to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the download, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/downloads/{id}")
    public ResponseEntity<Download> getDownload(@PathVariable Long id) {
        log.debug("REST request to get Download : {}", id);
        Optional<Download> download = downloadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(download);
    }

    /**
     * {@code DELETE  /downloads/:id} : delete the "id" download.
     *
     * @param id the id of the download to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/downloads/{id}")
    public ResponseEntity<Void> deleteDownload(@PathVariable Long id) {
        log.debug("REST request to delete Download : {}", id);
        downloadRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
