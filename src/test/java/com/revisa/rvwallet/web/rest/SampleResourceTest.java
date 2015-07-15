package com.revisa.rvwallet.web.rest;

import com.revisa.rvwallet.Application;
import com.revisa.rvwallet.domain.Sample;
import com.revisa.rvwallet.repository.SampleRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SampleResource REST controller.
 *
 * @see SampleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SampleResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private SampleRepository sampleRepository;

    private MockMvc restSampleMockMvc;

    private Sample sample;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SampleResource sampleResource = new SampleResource();
        ReflectionTestUtils.setField(sampleResource, "sampleRepository", sampleRepository);
        this.restSampleMockMvc = MockMvcBuilders.standaloneSetup(sampleResource).build();
    }

    @Before
    public void initTest() {
        sample = new Sample();
        sample.setName(DEFAULT_NAME);
        sample.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSample() throws Exception {
        int databaseSizeBeforeCreate = sampleRepository.findAll().size();

        // Create the Sample
        restSampleMockMvc.perform(post("/api/samples")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sample)))
                .andExpect(status().isCreated());

        // Validate the Sample in the database
        List<Sample> samples = sampleRepository.findAll();
        assertThat(samples).hasSize(databaseSizeBeforeCreate + 1);
        Sample testSample = samples.get(samples.size() - 1);
        assertThat(testSample.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSample.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSamples() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the samples
        restSampleMockMvc.perform(get("/api/samples"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sample.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get the sample
        restSampleMockMvc.perform(get("/api/samples/{id}", sample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sample.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSample() throws Exception {
        // Get the sample
        restSampleMockMvc.perform(get("/api/samples/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

		int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Update the sample
        sample.setName(UPDATED_NAME);
        sample.setDescription(UPDATED_DESCRIPTION);
        restSampleMockMvc.perform(put("/api/samples")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sample)))
                .andExpect(status().isOk());

        // Validate the Sample in the database
        List<Sample> samples = sampleRepository.findAll();
        assertThat(samples).hasSize(databaseSizeBeforeUpdate);
        Sample testSample = samples.get(samples.size() - 1);
        assertThat(testSample.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSample.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

		int databaseSizeBeforeDelete = sampleRepository.findAll().size();

        // Get the sample
        restSampleMockMvc.perform(delete("/api/samples/{id}", sample.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sample> samples = sampleRepository.findAll();
        assertThat(samples).hasSize(databaseSizeBeforeDelete - 1);
    }
}
