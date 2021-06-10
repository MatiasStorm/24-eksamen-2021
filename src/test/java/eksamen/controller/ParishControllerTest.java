package eksamen.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import eksamen.model.Commune;
import eksamen.model.Parish;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(controllers = ParishController.class)
public class ParishControllerTest extends AbstractRestControllerTest {

    @Test 
    public void createWithCorrectRequestBody() throws Exception{
        Commune commune = new Commune(101, "København");
        Parish parish = new Parish(200, 2.3, "København sogn", LocalDate.now());
        parish.setCommune(commune);
        String inputJson = mapToJson(parish);

        mockMvc.perform(post("/sogn")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
            .andExpect(status().isCreated());

    }

}
