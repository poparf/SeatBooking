package popa.robert.seatbooking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import popa.robert.seatbooking.controller.MovieController;
import popa.robert.seatbooking.model.Movie;
import popa.robert.seatbooking.repository.MovieRepository;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MovieRepository movieRepository;

    private final String baseUrl = "/api/movies";


    @Test
    public void test_endpoint_without_being_auth() throws Exception {
        mvc.perform(get("/api/test")).andExpect(status().isUnauthorized());
    }

    @Test// When i mock a user the tests skip auth phase
    // and fills the security context with it
    @WithMockUser(roles = {"ADMIN"})
    public void test_endpoint_with_being_auth() throws Exception {
        mvc.perform(get("/api/test")).andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void should_create_a_movie() throws Exception {
        MockMultipartFile posterFile = new MockMultipartFile("posterImage", "poster.jpg", "image/jpeg", "some-image".getBytes());

        mvc.perform(multipart( baseUrl)
                        .file(posterFile)
                        .param("title", "Test Movie")
                        .param("description", "Test Description")
                        .param("playTime", "PT2H"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/movies/Test%20Movie"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void should_retrieve_page_movies() throws Exception {
        Movie m1 = new Movie("movie_1", "m_desc_1", Duration.parse("PT2H"), null);
        Movie m2 = new Movie("movie_2", "m_desc_2", Duration.parse("PT2H30M"), null);

        Page<Movie> moviePage = new PageImpl<>(List.of(m1,m2), PageRequest.of(0, 2), 2);
        Mockito.when(movieRepository.findAll(Mockito.any(Pageable.class))).thenReturn(moviePage);

        MvcResult result = mvc.perform(get(baseUrl)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn();

        String resBody = result.getResponse().getContentAsString();

        // Deserialize the hashmap from json
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = mapper.readValue(resBody, new TypeReference<HashMap<String,Object>>(){});

        assertThat(map).containsKey("movies");
        assertThat(map).containsKey("totalPages");
        assertThat(map).containsKey("currentPage");

        List<Map<String, Object>> returnedMovies = (List<Map<String, Object>>) map.get("movies");
        assertEquals(2, returnedMovies.size());
        assertEquals("movie_1", returnedMovies.get(0).get("title"));
        assertEquals("movie_2", returnedMovies.get(1).get("title"));
        assertEquals(1, map.get("totalPages"));
        assertEquals(0, map.get("currentPage"));
    }
}
