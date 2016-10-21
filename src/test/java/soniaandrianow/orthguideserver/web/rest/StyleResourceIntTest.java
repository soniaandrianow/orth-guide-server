package soniaandrianow.orthguideserver.web.rest;

import soniaandrianow.orthguideserver.OrthGuideApp;

import soniaandrianow.orthguideserver.domain.Style;
import soniaandrianow.orthguideserver.repository.StyleRepository;

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
 * Test class for the StyleResource REST controller.
 *
 * @see StyleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrthGuideApp.class)
public class StyleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private StyleRepository styleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restStyleMockMvc;

    private Style style;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StyleResource styleResource = new StyleResource();
        ReflectionTestUtils.setField(styleResource, "styleRepository", styleRepository);
        this.restStyleMockMvc = MockMvcBuilders.standaloneSetup(styleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Style createEntity(EntityManager em) {
        Style style = new Style()
                .name(DEFAULT_NAME);
        return style;
    }

    @Before
    public void initTest() {
        style = createEntity(em);
    }

    @Test
    @Transactional
    public void createStyle() throws Exception {
        int databaseSizeBeforeCreate = styleRepository.findAll().size();

        // Create the Style

        restStyleMockMvc.perform(post("/api/styles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(style)))
                .andExpect(status().isCreated());

        // Validate the Style in the database
        List<Style> styles = styleRepository.findAll();
        assertThat(styles).hasSize(databaseSizeBeforeCreate + 1);
        Style testStyle = styles.get(styles.size() - 1);
        assertThat(testStyle.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = styleRepository.findAll().size();
        // set the field null
        style.setName(null);

        // Create the Style, which fails.

        restStyleMockMvc.perform(post("/api/styles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(style)))
                .andExpect(status().isBadRequest());

        List<Style> styles = styleRepository.findAll();
        assertThat(styles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStyles() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        // Get all the styles
        restStyleMockMvc.perform(get("/api/styles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(style.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStyle() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        // Get the style
        restStyleMockMvc.perform(get("/api/styles/{id}", style.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(style.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStyle() throws Exception {
        // Get the style
        restStyleMockMvc.perform(get("/api/styles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStyle() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();

        // Update the style
        Style updatedStyle = styleRepository.findOne(style.getId());
        updatedStyle
                .name(UPDATED_NAME);

        restStyleMockMvc.perform(put("/api/styles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStyle)))
                .andExpect(status().isOk());

        // Validate the Style in the database
        List<Style> styles = styleRepository.findAll();
        assertThat(styles).hasSize(databaseSizeBeforeUpdate);
        Style testStyle = styles.get(styles.size() - 1);
        assertThat(testStyle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteStyle() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);
        int databaseSizeBeforeDelete = styleRepository.findAll().size();

        // Get the style
        restStyleMockMvc.perform(delete("/api/styles/{id}", style.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Style> styles = styleRepository.findAll();
        assertThat(styles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
