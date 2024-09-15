package popa.robert.seatbooking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    // When i mock a user the tests skip auth phase
    // and fills the security context with it

    private final String baseUrl = "/api";
    @Test
    public void test_endpoint_without_being_auth() throws Exception {
        mvc.perform(get(baseUrl +"/test")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void test_endpoint_with_being_auth() throws Exception {
        mvc.perform(get(baseUrl + "/test")).andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateMovie() throws Exception {
        MockMultipartFile posterFile = new MockMultipartFile("posterImage", "poster.jpg", "image/jpeg", "some-image".getBytes());

        mvc.perform(multipart(baseUrl + "/movies")
                        .file(posterFile)
                        .param("title", "Test Movie")
                        .param("description", "Test Description")
                        .param("playTime", "PT2H"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/movies/Test%20Movie"));
    }
}
