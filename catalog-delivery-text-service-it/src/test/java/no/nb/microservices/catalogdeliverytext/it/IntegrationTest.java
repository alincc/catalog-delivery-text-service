package no.nb.microservices.catalogdeliverytext.it;

import no.nb.microservices.catalogdeliverytext.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class IntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void whenAltoIsFoundResponseShouldBeOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0006"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void whenAltoIsNotFoundResponseShouldBeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0060"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }
}
