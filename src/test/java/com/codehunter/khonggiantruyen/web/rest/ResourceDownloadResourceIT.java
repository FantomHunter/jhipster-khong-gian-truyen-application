package com.codehunter.khonggiantruyen.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehunter.khonggiantruyen.IntegrationTest;
import com.codehunter.khonggiantruyen.domain.Product;
import com.codehunter.khonggiantruyen.domain.ResourceDownload;
import com.codehunter.khonggiantruyen.domain.enumeration.FormatType;
import com.codehunter.khonggiantruyen.repository.ResourceDownloadRepository;
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
 * Integration tests for the {@link ResourceDownloadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourceDownloadResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final FormatType DEFAULT_FORMAT = FormatType.PRC;
    private static final FormatType UPDATED_FORMAT = FormatType.PDF;

    private static final String ENTITY_API_URL = "/api/resource-downloads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResourceDownloadRepository resourceDownloadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceDownloadMockMvc;

    private ResourceDownload resourceDownload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceDownload createEntity(EntityManager em) {
        ResourceDownload resourceDownload = new ResourceDownload().url(DEFAULT_URL).format(DEFAULT_FORMAT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        resourceDownload.setProduct(product);
        return resourceDownload;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceDownload createUpdatedEntity(EntityManager em) {
        ResourceDownload resourceDownload = new ResourceDownload().url(UPDATED_URL).format(UPDATED_FORMAT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        resourceDownload.setProduct(product);
        return resourceDownload;
    }

    @BeforeEach
    public void initTest() {
        resourceDownload = createEntity(em);
    }

    @Test
    @Transactional
    void createResourceDownload() throws Exception {
        int databaseSizeBeforeCreate = resourceDownloadRepository.findAll().size();
        // Create the ResourceDownload
        restResourceDownloadMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isCreated());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceDownload testResourceDownload = resourceDownloadList.get(resourceDownloadList.size() - 1);
        assertThat(testResourceDownload.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testResourceDownload.getFormat()).isEqualTo(DEFAULT_FORMAT);
    }

    @Test
    @Transactional
    void createResourceDownloadWithExistingId() throws Exception {
        // Create the ResourceDownload with an existing ID
        resourceDownload.setId(1L);

        int databaseSizeBeforeCreate = resourceDownloadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceDownloadMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceDownloadRepository.findAll().size();
        // set the field null
        resourceDownload.setUrl(null);

        // Create the ResourceDownload, which fails.

        restResourceDownloadMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isBadRequest());

        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResourceDownloads() throws Exception {
        // Initialize the database
        resourceDownloadRepository.saveAndFlush(resourceDownload);

        // Get all the resourceDownloadList
        restResourceDownloadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceDownload.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())));
    }

    @Test
    @Transactional
    void getResourceDownload() throws Exception {
        // Initialize the database
        resourceDownloadRepository.saveAndFlush(resourceDownload);

        // Get the resourceDownload
        restResourceDownloadMockMvc
            .perform(get(ENTITY_API_URL_ID, resourceDownload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceDownload.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingResourceDownload() throws Exception {
        // Get the resourceDownload
        restResourceDownloadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResourceDownload() throws Exception {
        // Initialize the database
        resourceDownloadRepository.saveAndFlush(resourceDownload);

        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();

        // Update the resourceDownload
        ResourceDownload updatedResourceDownload = resourceDownloadRepository.findById(resourceDownload.getId()).get();
        // Disconnect from session so that the updates on updatedResourceDownload are not directly saved in db
        em.detach(updatedResourceDownload);
        updatedResourceDownload.url(UPDATED_URL).format(UPDATED_FORMAT);

        restResourceDownloadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResourceDownload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResourceDownload))
            )
            .andExpect(status().isOk());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
        ResourceDownload testResourceDownload = resourceDownloadList.get(resourceDownloadList.size() - 1);
        assertThat(testResourceDownload.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testResourceDownload.getFormat()).isEqualTo(UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void putNonExistingResourceDownload() throws Exception {
        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();
        resourceDownload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceDownloadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceDownload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResourceDownload() throws Exception {
        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();
        resourceDownload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceDownloadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResourceDownload() throws Exception {
        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();
        resourceDownload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceDownloadMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourceDownloadWithPatch() throws Exception {
        // Initialize the database
        resourceDownloadRepository.saveAndFlush(resourceDownload);

        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();

        // Update the resourceDownload using partial update
        ResourceDownload partialUpdatedResourceDownload = new ResourceDownload();
        partialUpdatedResourceDownload.setId(resourceDownload.getId());

        partialUpdatedResourceDownload.url(UPDATED_URL).format(UPDATED_FORMAT);

        restResourceDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceDownload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceDownload))
            )
            .andExpect(status().isOk());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
        ResourceDownload testResourceDownload = resourceDownloadList.get(resourceDownloadList.size() - 1);
        assertThat(testResourceDownload.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testResourceDownload.getFormat()).isEqualTo(UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void fullUpdateResourceDownloadWithPatch() throws Exception {
        // Initialize the database
        resourceDownloadRepository.saveAndFlush(resourceDownload);

        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();

        // Update the resourceDownload using partial update
        ResourceDownload partialUpdatedResourceDownload = new ResourceDownload();
        partialUpdatedResourceDownload.setId(resourceDownload.getId());

        partialUpdatedResourceDownload.url(UPDATED_URL).format(UPDATED_FORMAT);

        restResourceDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceDownload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceDownload))
            )
            .andExpect(status().isOk());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
        ResourceDownload testResourceDownload = resourceDownloadList.get(resourceDownloadList.size() - 1);
        assertThat(testResourceDownload.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testResourceDownload.getFormat()).isEqualTo(UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void patchNonExistingResourceDownload() throws Exception {
        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();
        resourceDownload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceDownload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResourceDownload() throws Exception {
        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();
        resourceDownload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResourceDownload() throws Exception {
        int databaseSizeBeforeUpdate = resourceDownloadRepository.findAll().size();
        resourceDownload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceDownloadMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceDownload))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceDownload in the database
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResourceDownload() throws Exception {
        // Initialize the database
        resourceDownloadRepository.saveAndFlush(resourceDownload);

        int databaseSizeBeforeDelete = resourceDownloadRepository.findAll().size();

        // Delete the resourceDownload
        restResourceDownloadMockMvc
            .perform(delete(ENTITY_API_URL_ID, resourceDownload.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResourceDownload> resourceDownloadList = resourceDownloadRepository.findAll();
        assertThat(resourceDownloadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
