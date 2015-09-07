package no.nb.microservices.catalogdeliverytext.rest.controller;

import no.nb.microservices.catalogdeliverytext.core.alto.IAltoService;
import no.nb.microservices.catalogdeliverytext.core.text.service.ITextService;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AltoControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private AltoController altoController;

    @Mock
    private ITextService textService;

    @Mock
    private IAltoService altoService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(altoController).build();
    }

    @Test
    public void testAlto() throws Exception{
        when(textService.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0001")).thenReturn(new Alto());
        mockMvc.perform(get("/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0001"))
                .andExpect(status().isOk());
    }

}
