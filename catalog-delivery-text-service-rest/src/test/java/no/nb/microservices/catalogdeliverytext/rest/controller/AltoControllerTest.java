package no.nb.microservices.catalogdeliverytext.rest.controller;

import no.nb.microservices.catalogdeliverytext.core.alto.service.IAltoService;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AltoControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private AltoController altoController;

    @Mock
    private IAltoService altoService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(altoController).build();
    }

    @Test
    public void testAlto() throws Exception{
        when(altoService.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0001")).thenReturn(new Alto());
        mockMvc.perform(get("/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0001"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAltos() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("5385811c-fe21-40b2-9b59-5f941df6f0bf.zip").getFile());

        when(altoService.getAltoFilesZipped(anyString(), anyString(), anyString())).thenReturn(file);
        mockMvc.perform(get("/alto/URN:NBN:no-nb_digibok_2014062307158"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAltosNotFound() throws Exception {
        when(altoService.getAltoFilesZipped(anyString(), anyString(), anyString())).thenThrow(new AltoNotFoundException("Not found"));
        mockMvc.perform(get("/alto/URN:NBN:no-nb_digibok_asdjsad"))
                .andExpect(status().isNotFound());
    }

}
