package no.nb.microservices.catalogdeliverytext.core.text.repository;

import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import static org.junit.Assert.assertNotNull;

public class AltoRepositoryTest {

    private AltoRepository altoRepository;

    @Before
    public void setup() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.nb.microservices.catalogdeliverytext.model");
        altoRepository = new DiskAltoRepository(marshaller);
    }

    @Test
    public void whenAltoIsFoundReponseShouldBeNotNull() {
        Alto altoList = altoRepository.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0004");
        assertNotNull(altoList);
    }

    @Test(expected = AltoNotFoundException.class)
    public void whenAltoNotFoundExceptionShouldBeThrown() {
        Alto alto = altoRepository.getAlto("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0040");
    }
}
