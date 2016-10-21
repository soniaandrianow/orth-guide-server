package soniaandrianow.orthguideserver.web.rest;

import soniaandrianow.orthguideserver.OrthGuideApp;

import soniaandrianow.orthguideserver.domain.Church;
import soniaandrianow.orthguideserver.domain.Diocese;
import soniaandrianow.orthguideserver.domain.Style;
import soniaandrianow.orthguideserver.repository.ChurchRepository;

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
 * Test class for the ChurchResource REST controller.
 *
 * @see ChurchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrthGuideApp.class)
public class ChurchResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final Double DEFAULT_LONGITUDE = -180.0D;
    private static final Double UPDATED_LONGITUDE = -179D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final String DEFAULT_FETE = "AAAAA";
    private static final String UPDATED_FETE = "BBBBB";

    private static final String DEFAULT_DEDICATION = "AAAAA";
    private static final String UPDATED_DEDICATION = "BBBBB";

    private static final String DEFAULT_PARSON = "AAAAA";
    private static final String UPDATED_PARSON = "BBBBB";

    private static final Integer DEFAULT_CENTURY = 1;
    private static final Integer UPDATED_CENTURY = 2;

    private static final Boolean DEFAULT_WOODEN = false;
    private static final Boolean UPDATED_WOODEN = true;

    private static final String DEFAULT_SERVICES = "AAAAA";
    private static final String UPDATED_SERVICES = "BBBBB";

    private static final String DEFAULT_SHORT_HISTORY = "AAAAA";
    private static final String UPDATED_SHORT_HISTORY = "BBBBB";

    @Inject
    private ChurchRepository churchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChurchMockMvc;

    private Church church;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChurchResource churchResource = new ChurchResource();
        ReflectionTestUtils.setField(churchResource, "churchRepository", churchRepository);
        this.restChurchMockMvc = MockMvcBuilders.standaloneSetup(churchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Church createEntity(EntityManager em) {
        Church church = new Church()
                .address(DEFAULT_ADDRESS)
                .longitude(DEFAULT_LONGITUDE)
                .latitude(DEFAULT_LATITUDE)
                .fete(DEFAULT_FETE)
                .dedication(DEFAULT_DEDICATION)
                .parson(DEFAULT_PARSON)
                .century(DEFAULT_CENTURY)
                .wooden(DEFAULT_WOODEN)
                .services(DEFAULT_SERVICES)
                .short_history(DEFAULT_SHORT_HISTORY);
        // Add required entity
        Diocese diocese_church = DioceseResourceIntTest.createEntity(em);
        em.persist(diocese_church);
        em.flush();
        church.setDiocese_church(diocese_church);
        // Add required entity
        Style church_style = StyleResourceIntTest.createEntity(em);
        em.persist(church_style);
        em.flush();
        church.setChurch_style(church_style);
        return church;
    }

    @Before
    public void initTest() {
        church = createEntity(em);
    }

    @Test
    @Transactional
    public void createChurch() throws Exception {
        int databaseSizeBeforeCreate = churchRepository.findAll().size();

        // Create the Church

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isCreated());

        // Validate the Church in the database
        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeCreate + 1);
        Church testChurch = churches.get(churches.size() - 1);
        assertThat(testChurch.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testChurch.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testChurch.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testChurch.getFete()).isEqualTo(DEFAULT_FETE);
        assertThat(testChurch.getDedication()).isEqualTo(DEFAULT_DEDICATION);
        assertThat(testChurch.getParson()).isEqualTo(DEFAULT_PARSON);
        assertThat(testChurch.getCentury()).isEqualTo(DEFAULT_CENTURY);
        assertThat(testChurch.isWooden()).isEqualTo(DEFAULT_WOODEN);
        assertThat(testChurch.getServices()).isEqualTo(DEFAULT_SERVICES);
        assertThat(testChurch.getShort_history()).isEqualTo(DEFAULT_SHORT_HISTORY);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setAddress(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setLongitude(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFeteIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setFete(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDedicationIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setDedication(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParsonIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setParson(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCenturyIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setCentury(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWoodenIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setWooden(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShort_historyIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setShort_history(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(church)))
                .andExpect(status().isBadRequest());

        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChurches() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churches
        restChurchMockMvc.perform(get("/api/churches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(church.getId().intValue())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].fete").value(hasItem(DEFAULT_FETE.toString())))
                .andExpect(jsonPath("$.[*].dedication").value(hasItem(DEFAULT_DEDICATION.toString())))
                .andExpect(jsonPath("$.[*].parson").value(hasItem(DEFAULT_PARSON.toString())))
                .andExpect(jsonPath("$.[*].century").value(hasItem(DEFAULT_CENTURY)))
                .andExpect(jsonPath("$.[*].wooden").value(hasItem(DEFAULT_WOODEN.booleanValue())))
                .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES.toString())))
                .andExpect(jsonPath("$.[*].short_history").value(hasItem(DEFAULT_SHORT_HISTORY.toString())));
    }

    @Test
    @Transactional
    public void getChurch() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get the church
        restChurchMockMvc.perform(get("/api/churches/{id}", church.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(church.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.fete").value(DEFAULT_FETE.toString()))
            .andExpect(jsonPath("$.dedication").value(DEFAULT_DEDICATION.toString()))
            .andExpect(jsonPath("$.parson").value(DEFAULT_PARSON.toString()))
            .andExpect(jsonPath("$.century").value(DEFAULT_CENTURY))
            .andExpect(jsonPath("$.wooden").value(DEFAULT_WOODEN.booleanValue()))
            .andExpect(jsonPath("$.services").value(DEFAULT_SERVICES.toString()))
            .andExpect(jsonPath("$.short_history").value(DEFAULT_SHORT_HISTORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChurch() throws Exception {
        // Get the church
        restChurchMockMvc.perform(get("/api/churches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChurch() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);
        int databaseSizeBeforeUpdate = churchRepository.findAll().size();

        // Update the church
        Church updatedChurch = churchRepository.findOne(church.getId());
        updatedChurch
                .address(UPDATED_ADDRESS)
                .longitude(UPDATED_LONGITUDE)
                .latitude(UPDATED_LATITUDE)
                .fete(UPDATED_FETE)
                .dedication(UPDATED_DEDICATION)
                .parson(UPDATED_PARSON)
                .century(UPDATED_CENTURY)
                .wooden(UPDATED_WOODEN)
                .services(UPDATED_SERVICES)
                .short_history(UPDATED_SHORT_HISTORY);

        restChurchMockMvc.perform(put("/api/churches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChurch)))
                .andExpect(status().isOk());

        // Validate the Church in the database
        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeUpdate);
        Church testChurch = churches.get(churches.size() - 1);
        assertThat(testChurch.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testChurch.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testChurch.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testChurch.getFete()).isEqualTo(UPDATED_FETE);
        assertThat(testChurch.getDedication()).isEqualTo(UPDATED_DEDICATION);
        assertThat(testChurch.getParson()).isEqualTo(UPDATED_PARSON);
        assertThat(testChurch.getCentury()).isEqualTo(UPDATED_CENTURY);
        assertThat(testChurch.isWooden()).isEqualTo(UPDATED_WOODEN);
        assertThat(testChurch.getServices()).isEqualTo(UPDATED_SERVICES);
        assertThat(testChurch.getShort_history()).isEqualTo(UPDATED_SHORT_HISTORY);
    }

    @Test
    @Transactional
    public void deleteChurch() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);
        int databaseSizeBeforeDelete = churchRepository.findAll().size();

        // Get the church
        restChurchMockMvc.perform(delete("/api/churches/{id}", church.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Church> churches = churchRepository.findAll();
        assertThat(churches).hasSize(databaseSizeBeforeDelete - 1);
    }
}
