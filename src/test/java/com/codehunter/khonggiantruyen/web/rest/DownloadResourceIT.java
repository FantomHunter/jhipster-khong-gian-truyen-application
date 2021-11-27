package com.codehunter.khonggiantruyen.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehunter.khonggiantruyen.IntegrationTest;
import com.codehunter.khonggiantruyen.domain.Download;
import com.codehunter.khonggiantruyen.domain.User;
import com.codehunter.khonggiantruyen.repository.DownloadRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DownloadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DownloadResourceIT {

    private static final Instant DEFAULT_DOWNLOAD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOWNLOAD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/downloads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DownloadRepository downloadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDownloadMockMvc;

    private Download download;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Download createEntity(EntityManager em) {
        Download download = new Download().downloadDate(DEFAULT_DOWNLOAD_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        download.setUser(user);
        return download;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Download createUpdatedEntity(EntityManager em) {
        Download download = new Download().downloadDate(UPDATED_DOWNLOAD_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        download.setUser(user);
        return download;
    }

    @BeforeEach
    public void initTest() {
        download = createEntity(em);
    }

    @Test
    @Transactional
    void createDownload() throws Exception {
        int databaseSizeBeforeCreate = downloadRepository.findAll().size();
        // Create the Download
        restDownloadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(download)))
            .andExpect(status().isCreated());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeCreate + 1);
        Download testDownload = downloadList.get(downloadList.size() - 1);
        assertThat(testDownload.getDownloadDate()).isEqualTo(DEFAULT_DOWNLOAD_DATE);
    }

    @Test
    @Transactional
    void createDownloadWithExistingId() throws Exception {
        // Create the Download with an existing ID
        download.setId(1L);

        int databaseSizeBeforeCreate = downloadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDownloadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(download)))
            .andExpect(status().isBadRequest());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDownloads() throws Exception {
        // Initialize the database
        downloadRepository.saveAndFlush(download);

        // Get all the downloadList
        restDownloadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(download.getId().intValue())))
            .andExpect(jsonPath("$.[*].downloadDate").value(hasItem(DEFAULT_DOWNLOAD_DATE.toString())));
    }

    @Test
    @Transactional
    void getDownload() throws Exception {
        // Initialize the database
        downloadRepository.saveAndFlush(download);

        // Get the download
        restDownloadMockMvc
            .perform(get(ENTITY_API_URL_ID, download.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(download.getId().intValue()))
            .andExpect(jsonPath("$.downloadDate").value(DEFAULT_DOWNLOAD_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDownload() throws Exception {
        // Get the download
        restDownloadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDownload() throws Exception {
        // Initialize the database
        downloadRepository.saveAndFlush(download);

        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();

        // Update the download
        Download updatedDownload = downloadRepository.findById(download.getId()).get();
        // Disconnect from session so that the updates on updatedDownload are not directly saved in db
        em.detach(updatedDownload);
        updatedDownload.downloadDate(UPDATED_DOWNLOAD_DATE);

        restDownloadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDownload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDownload))
            )
            .andExpect(status().isOk());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
        Download testDownload = downloadList.get(downloadList.size() - 1);
        assertThat(testDownload.getDownloadDate()).isEqualTo(UPDATED_DOWNLOAD_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDownload() throws Exception {
        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();
        download.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDownloadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, download.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(download))
            )
            .andExpect(status().isBadRequest());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDownload() throws Exception {
        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();
        download.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDownloadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(download))
            )
            .andExpect(status().isBadRequest());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDownload() throws Exception {
        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();
        download.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDownloadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(download)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDownloadWithPatch() throws Exception {
        // Initialize the database
        downloadRepository.saveAndFlush(download);

        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();

        // Update the download using partial update
        Download partialUpdatedDownload = new Download();
        partialUpdatedDownload.setId(download.getId());

        partialUpdatedDownload.downloadDate(UPDATED_DOWNLOAD_DATE);

        restDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDownload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDownload))
            )
            .andExpect(status().isOk());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
        Download testDownload = downloadList.get(downloadList.size() - 1);
        assertThat(testDownload.getDownloadDate()).isEqualTo(UPDATED_DOWNLOAD_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDownloadWithPatch() throws Exception {
        // Initialize the database
        downloadRepository.saveAndFlush(download);

        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();

        // Update the download using partial update
        Download partialUpdatedDownload = new Download();
        partialUpdatedDownload.setId(download.getId());

        partialUpdatedDownload.downloadDate(UPDATED_DOWNLOAD_DATE);

        restDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDownload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDownload))
            )
            .andExpect(status().isOk());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
        Download testDownload = downloadList.get(downloadList.size() - 1);
        assertThat(testDownload.getDownloadDate()).isEqualTo(UPDATED_DOWNLOAD_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDownload() throws Exception {
        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();
        download.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, download.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(download))
            )
            .andExpect(status().isBadRequest());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDownload() throws Exception {
        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();
        download.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(download))
            )
            .andExpect(status().isBadRequest());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDownload() throws Exception {
        int databaseSizeBeforeUpdate = downloadRepository.findAll().size();
        download.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDownloadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(download)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Download in the database
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDownload() throws Exception {
        // Initialize the database
        downloadRepository.saveAndFlush(download);

        int databaseSizeBeforeDelete = downloadRepository.findAll().size();

        // Delete the download
        restDownloadMockMvc
            .perform(delete(ENTITY_API_URL_ID, download.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Download> downloadList = downloadRepository.findAll();
        assertThat(downloadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
