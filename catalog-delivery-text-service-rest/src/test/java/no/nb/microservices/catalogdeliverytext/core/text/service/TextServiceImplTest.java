package no.nb.microservices.catalogdeliverytext.core.text.service;

import no.nb.microservices.catalogdeliverytext.core.text.repository.AltoRepository;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TextServiceImplTest {

    @Mock
    private AltoRepository altoRepository;

    private ITextService textService;

    @Before
    public void setup() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.nb.microservices.catalogdeliverytext.model");
        textService = new TextServiceImpl(altoRepository, marshaller);
    }

    @Test(expected = AltoNotFoundException.class)
    public void whenAltoIsNotFoundThenExceptionShouldBeThrown() throws Exception {
        when(altoRepository.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0040")).thenReturn(null);
        textService.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0040");
    }

    @Test
    public void whenAltoIsFoundResponseShouldBeNotNull() {
        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        when(altoRepository.getAlto("URN:NBN:no-nb_digibok_2014062307158","URN:NBN:no-nb_digibok_2014062307158_0004")).thenReturn(new File(path + "/alto/urn:nbn:no-nb_digibok_2014062307158/digibok_2014062307158_0004.xml"));
        Alto altoFile = textService.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0004");
        assertNotNull(altoFile);

        verify(altoRepository).getAlto("URN:NBN:no-nb_digibok_2014062307158","URN:NBN:no-nb_digibok_2014062307158_0004");
        verifyNoMoreInteractions(altoRepository);
    }

}
