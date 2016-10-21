package soniaandrianow.orthguideserver.web.rest;

import soniaandrianow.orthguideserver.OrthGuideApp;

import soniaandrianow.orthguideserver.domain.Diocese;
import soniaandrianow.orthguideserver.repository.DioceseRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DioceseResource REST controller.
 *
 * @see DioceseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrthGuideApp.class)
public class DioceseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private DioceseRepository dioceseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDioceseMockMvc;

    private Diocese diocese;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DioceseResource dioceseResource = new DioceseResource();
        ReflectionTestUtils.setField(dioceseResource, "dioceseRepository", dioceseRepository);
        this.restDioceseMockMvc = MockMvcBuilders.standaloneSetup(dioceseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diocese createEntity(EntityManager em) {
        Diocese diocese = new Diocese()
                .name(DEFAULT_NAME);
        return diocese;
    }

    @Before
    public void initTest() {
        diocese = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiocese() throws Exception {
        int databaseSizeBeforeCreate = dioceseRepository.findAll().size();

        // Create the Diocese

        restDioceseMockMvc.perform(post("/api/diocese")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diocese)))
                .andExpect(status().isCreated());

        // Validate the Diocese in the database
        List<Diocese> diocese = dioceseRepository.findAll();
        assertThat(diocese).hasSize(databaseSizeBeforeCreate + 1);
        Diocese testDiocese = diocese.get(diocese.size() - 1);
        assertThat(testDiocese.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dioceseRepository.findAll().size();
        // set the field null
        diocese.setName(null);

        // Create the Diocese, which fails.

        restDioceseMockMvc.perform(post("/api/diocese")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diocese)))
                .andExpect(status().isBadRequest());

        List<Diocese> diocese = dioceseRepository.findAll();
        assertThat(diocese).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiocese() throws Exception {
        // Initialize the database
        dioceseRepository.saveAndFlush(diocese);

        // Get all the diocese
        restDioceseMockMvc.perform(get("/api/diocese?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(diocese.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDiocese() throws Exception {
        // Initialize the database
        dioceseRepository.saveAndFlush(diocese);

        // Get the diocese
        restDioceseMockMvc.perform(get("/api/diocese/{id}", diocese.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diocese.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiocese() throws Exception {
        // Get the diocese
        restDioceseMockMvc.perform(get("/api/diocese/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiocese() throws Exception {
        // Initialize the database
        dioceseRepository.saveAndFlush(diocese);
        int databaseSizeBeforeUpdate = dioceseRepository.findAll().size();

        // Update the diocese
        Diocese updatedDiocese = dioceseRepository.findOne(diocese.getId());
        updatedDiocese
                .name(UPDATED_NAME);

        restDioceseMockMvc.perform(put("/api/diocese")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDiocese)))
                .andExpect(status().isOk());

        // Validate the Diocese in the database
        List<Diocese> diocese = dioceseRepository.findAll();
        assertThat(diocese).hasSize(databaseSizeBeforeUpdate);
        Diocese testDiocese = diocese.get(diocese.size() - 1);
        assertThat(testDiocese.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteDiocese() throws Exception {
        // Initialize the database
        dioceseRepository.saveAndFlush(diocese);
        int databaseSizeBeforeDelete = dioceseRepository.findAll().size();

        // Get the diocese
        restDioceseMockMvc.perform(delete("/api/diocese/{id}", diocese.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Diocese> diocese = dioceseRepository.findAll();
        assertThat(diocese).hasSize(databaseSizeBeforeDelete - 1);
    }
}
