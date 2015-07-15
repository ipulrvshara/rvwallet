package com.revisa.rvwallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.revisa.rvwallet.domain.Sample;
import com.revisa.rvwallet.repository.SampleRepository;
import com.revisa.rvwallet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sample.
 */
@RestController
@RequestMapping("/api")
public class SampleResource {

    private final Logger log = LoggerFactory.getLogger(SampleResource.class);

    @Inject
    private SampleRepository sampleRepository;

    /**
     * POST  /samples -> Create a new sample.
     */
    @RequestMapping(value = "/samples",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sample> create(@RequestBody Sample sample) throws URISyntaxException {
        log.debug("REST request to save Sample : {}", sample);
        if (sample.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new sample cannot already have an ID").body(null);
        }
        Sample result = sampleRepository.save(sample);
        return ResponseEntity.created(new URI("/api/samples/" + sample.getId())).body(result);
    }

    /**
     * PUT  /samples -> Updates an existing sample.
     */
    @RequestMapping(value = "/samples",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sample> update(@RequestBody Sample sample) throws URISyntaxException {
        log.debug("REST request to update Sample : {}", sample);
        if (sample.getId() == null) {
            return create(sample);
        }
        Sample result = sampleRepository.save(sample);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /samples -> get all the samples.
     */
    @RequestMapping(value = "/samples",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sample>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Sample> page = sampleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/samples", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /samples/:id -> get the "id" sample.
     */
    @RequestMapping(value = "/samples/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sample> get(@PathVariable Long id) {
        log.debug("REST request to get Sample : {}", id);
        return Optional.ofNullable(sampleRepository.findOne(id))
            .map(sample -> new ResponseEntity<>(
                sample,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /samples/:id -> delete the "id" sample.
     */
    @RequestMapping(value = "/samples/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Sample : {}", id);
        sampleRepository.delete(id);
    }
}
