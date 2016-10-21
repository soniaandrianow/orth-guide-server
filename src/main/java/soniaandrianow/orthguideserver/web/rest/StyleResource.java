package soniaandrianow.orthguideserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import soniaandrianow.orthguideserver.domain.Style;

import soniaandrianow.orthguideserver.repository.StyleRepository;
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
 * REST controller for managing Style.
 */
@RestController
@RequestMapping("/api")
public class StyleResource {

    private final Logger log = LoggerFactory.getLogger(StyleResource.class);
        
    @Inject
    private StyleRepository styleRepository;

    /**
     * POST  /styles : Create a new style.
     *
     * @param style the style to create
     * @return the ResponseEntity with status 201 (Created) and with body the new style, or with status 400 (Bad Request) if the style has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/styles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Style> createStyle(@Valid @RequestBody Style style) throws URISyntaxException {
        log.debug("REST request to save Style : {}", style);
        if (style.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("style", "idexists", "A new style cannot already have an ID")).body(null);
        }
        Style result = styleRepository.save(style);
        return ResponseEntity.created(new URI("/api/styles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("style", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /styles : Updates an existing style.
     *
     * @param style the style to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated style,
     * or with status 400 (Bad Request) if the style is not valid,
     * or with status 500 (Internal Server Error) if the style couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/styles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Style> updateStyle(@Valid @RequestBody Style style) throws URISyntaxException {
        log.debug("REST request to update Style : {}", style);
        if (style.getId() == null) {
            return createStyle(style);
        }
        Style result = styleRepository.save(style);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("style", style.getId().toString()))
            .body(result);
    }

    /**
     * GET  /styles : get all the styles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of styles in body
     */
    @RequestMapping(value = "/styles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Style> getAllStyles() {
        log.debug("REST request to get all Styles");
        List<Style> styles = styleRepository.findAll();
        return styles;
    }

    /**
     * GET  /styles/:id : get the "id" style.
     *
     * @param id the id of the style to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the style, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/styles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Style> getStyle(@PathVariable Long id) {
        log.debug("REST request to get Style : {}", id);
        Style style = styleRepository.findOne(id);
        return Optional.ofNullable(style)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /styles/:id : delete the "id" style.
     *
     * @param id the id of the style to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/styles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStyle(@PathVariable Long id) {
        log.debug("REST request to delete Style : {}", id);
        styleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("style", id.toString())).build();
    }

}
