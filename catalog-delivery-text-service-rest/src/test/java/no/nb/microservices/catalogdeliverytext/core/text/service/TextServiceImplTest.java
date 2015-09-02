package no.nb.microservices.catalogdeliverytext.core.text.service;

import no.nb.microservices.catalogdeliverytext.core.text.repository.AltoRepository;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TextServiceImplTest {

    @Mock
    private AltoRepository altoRepository;

    private ITextService textService;

    @Before
    public void setup() {
        textService = new TextServiceImpl(altoRepository);
    }

    @Test(expected = AltoNotFoundException.class)
    public void whenAltoIsNotFoundThenExceptionShouldBeThrown() throws Exception {
        when(altoRepository.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0040")).thenReturn(null);
        textService.getAltoFile("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0040");
    }

    @Test
    public void whenAltoIsFoundResponseShouldBeNotNull() {
        when(altoRepository.getAlto("URN:NBN:no-nb_digibok_2014062307158","URN:NBN:no-nb_digibok_2014062307158_0004")).thenReturn(new Alto());
        Alto altoFile = textService.getAltoFile("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0004");
        assertNotNull(altoFile);

        verify(altoRepository).getAlto("URN:NBN:no-nb_digibok_2014062307158","URN:NBN:no-nb_digibok_2014062307158_0004");
        verifyNoMoreInteractions(altoRepository);
    }

}
