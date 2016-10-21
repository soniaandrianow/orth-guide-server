package soniaandrianow.orthguideserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import soniaandrianow.orthguideserver.domain.Diocese;

import soniaandrianow.orthguideserver.repository.DioceseRepository;
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
 * REST controller for managing Diocese.
 */
@RestController
@RequestMapping("/api")
public class DioceseResource {

    private final Logger log = LoggerFactory.getLogger(DioceseResource.class);
        
    @Inject
    private DioceseRepository dioceseRepository;

    /**
     * POST  /diocese : Create a new diocese.
     *
     * @param diocese the diocese to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diocese, or with status 400 (Bad Request) if the diocese has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/diocese",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diocese> createDiocese(@Valid @RequestBody Diocese diocese) throws URISyntaxException {
        log.debug("REST request to save Diocese : {}", diocese);
        if (diocese.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("diocese", "idexists", "A new diocese cannot already have an ID")).body(null);
        }
        Diocese result = dioceseRepository.save(diocese);
        return ResponseEntity.created(new URI("/api/diocese/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("diocese", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diocese : Updates an existing diocese.
     *
     * @param diocese the diocese to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diocese,
     * or with status 400 (Bad Request) if the diocese is not valid,
     * or with status 500 (Internal Server Error) if the diocese couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/diocese",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diocese> updateDiocese(@Valid @RequestBody Diocese diocese) throws URISyntaxException {
        log.debug("REST request to update Diocese : {}", diocese);
        if (diocese.getId() == null) {
            return createDiocese(diocese);
        }
        Diocese result = dioceseRepository.save(diocese);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("diocese", diocese.getId().toString()))
            .body(result);
    }

    /**
     * GET  /diocese : get all the diocese.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of diocese in body
     */
    @RequestMapping(value = "/diocese",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Diocese> getAllDiocese() {
        log.debug("REST request to get all Diocese");
        List<Diocese> diocese = dioceseRepository.findAll();
        return diocese;
    }

    /**
     * GET  /diocese/:id : get the "id" diocese.
     *
     * @param id the id of the diocese to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diocese, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/diocese/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diocese> getDiocese(@PathVariable Long id) {
        log.debug("REST request to get Diocese : {}", id);
        Diocese diocese = dioceseRepository.findOne(id);
        return Optional.ofNullable(diocese)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /diocese/:id : delete the "id" diocese.
     *
     * @param id the id of the diocese to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/diocese/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiocese(@PathVariable Long id) {
        log.debug("REST request to delete Diocese : {}", id);
        dioceseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diocese", id.toString())).build();
    }

}
