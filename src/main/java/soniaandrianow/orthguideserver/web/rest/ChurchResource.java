package soniaandrianow.orthguideserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import soniaandrianow.orthguideserver.domain.Church;

import soniaandrianow.orthguideserver.repository.ChurchRepository;
import soniaandrianow.orthguideserver.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Church.
 */
@RestController
@RequestMapping("/api")
public class ChurchResource {

    private final Logger log = LoggerFactory.getLogger(ChurchResource.class);
        
    @Inject
    private ChurchRepository churchRepository;

    /**
     * POST  /churches : Create a new church.
     *
     * @param church the church to create
     * @return the ResponseEntity with status 201 (Created) and with body the new church, or with status 400 (Bad Request) if the church has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/churches",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Church> createChurch(@Valid @RequestBody Church church) throws URISyntaxException {
        log.debug("REST request to save Church : {}", church);
        if (church.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("church", "idexists", "A new church cannot already have an ID")).body(null);
        }
        Church result = churchRepository.save(church);
        return ResponseEntity.created(new URI("/api/churches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("church", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /churches : Updates an existing church.
     *
     * @param church the church to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated church,
     * or with status 400 (Bad Request) if the church is not valid,
     * or with status 500 (Internal Server Error) if the church couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/churches",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Church> updateChurch(@Valid @RequestBody Church church) throws URISyntaxException {
        log.debug("REST request to update Church : {}", church);
        if (church.getId() == null) {
            return createChurch(church);
        }
        Church result = churchRepository.save(church);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("church", church.getId().toString()))
            .body(result);
    }

    /**
     * GET  /churches : get all the churches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of churches in body
     */
    @RequestMapping(value = "/churches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Church> getAllChurches() {
        log.debug("REST request to get all Churches");
        List<Church> churches = churchRepository.findAll();
        return churches;
    }

    /**
     * GET  /churches/:id : get the "id" church.
     *
     * @param id the id of the church to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the church, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/churches/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Church> getChurch(@PathVariable Long id) {
        log.debug("REST request to get Church : {}", id);
        Church church = churchRepository.findOne(id);
        return Optional.ofNullable(church)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /churches/:id : delete the "id" church.
     *
     * @param id the id of the church to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/churches/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChurch(@PathVariable Long id) {
        log.debug("REST request to delete Church : {}", id);
        churchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("church", id.toString())).build();
    }

}
