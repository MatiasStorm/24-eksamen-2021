package eksamen.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eksamen.Application;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ParishControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test 
    public void createWithCorrectRequestBody() throws Exception{
        String json = "{ \"parishCode\": 3223,"
                    + "\"infectionRate\": 2,"
                    + "\"commune\": 157,"
                    + "\"name\": \"NYYYY\","
                    + "\"closing\": \"2021-06-12\""
                    + "}";
        mockMvc.perform(post("/sogn")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
            .andExpect(status().isCreated());
    }

    @Test 
    public void createWithUsedParishCodeRequestBody() throws Exception{
        String json = "{ \"parishCode\": 2,"
                    + "\"infectionRate\": 2,"
                    + "\"commune\": 157,"
                    + "\"name\": \"NYYYY\","
                    + "\"closing\": \"2021-06-12\""
                    + "}";
        mockMvc.perform(post("/sogn")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test 
    public void createWithUnkownCommuneRequestBody() throws Exception{
        String json = "{ \"parishCode\": 2,"
                    + "\"infectionRate\": 2,"
                    + "\"commune\": 157000,"
                    + "\"name\": \"NYYYY\","
                    + "\"closing\": \"2021-06-12\""
                    + "}";
        mockMvc.perform(post("/sogn")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test 
    public void putWithCorrectRequestBody() throws Exception{
        String json = "{ \"id\": 1, " 
                    + "\"parishCode\": 32,"
                    + "\"infectionRate\": 2,"
                    + "\"commune\": 157,"
                    + "\"name\": \"new Name\","
                    + "\"closing\": \"2021-06-12\""
                    + "}";
        mockMvc.perform(put("/sogn")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
            .andExpect(status().isCreated());
    }

    @Test 
    public void putWithUnkownCommuneRequestBody() throws Exception{
        String json = "{ \"id\": 1, " 
                    + "\"parishCode\": 32,"
                    + "\"infectionRate\": 2,"
                    + "\"commune\": 157000,"
                    + "\"name\": \"NYYYY\","
                    + "\"closing\": \"2021-06-12\""
                    + "}";
        mockMvc.perform(put("/sogn")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test 
    public void putWithUsedParishCodeRequestBody() throws Exception{
        String json = "{ \"id\": 1, " 
                    + "\"parishCode\": 2,"
                    + "\"infectionRate\": 2,"
                    + "\"commune\": 157000,"
                    + "\"name\": \"NYYYY\","
                    + "\"closing\": \"2021-06-12\""
                    + "}";
        mockMvc.perform(put("/sogn")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
            .andExpect(status().isBadRequest());
    }

    @Test 
    public void deleteExistingParish() throws Exception{
        mockMvc.perform(delete("/sogn/1"))
            .andExpect(status().isNoContent());
    }

    @Test 
    public void getAll() throws Exception{
        mockMvc.perform(get("/sogn"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("2021-06-09")));
    }

    // @Test 
    // public void getById() throws Exception{
    //     mockMvc.perform(get("/sogn/1"))
    //         .andExpect(status().isOk())
    //         .andExpect(content().string(containsString("København")));
    // }
}
