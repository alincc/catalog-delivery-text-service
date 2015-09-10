package no.nb.microservices.catalogdeliverytext.core.alto.repository;

import no.nb.microservices.catalogdeliverytext.core.alto.repository.AltoRepository;
import no.nb.microservices.catalogdeliverytext.core.alto.repository.DiskAltoRepository;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;

public class AltoRepositoryTest {

    private AltoRepository altoRepository;

    @Before
    public void setup() {
        altoRepository = new DiskAltoRepository();
    }

    @Test
    public void whenAltoIsFoundReponseShouldBeNotNull() {
        File altoList = altoRepository.getAltoFile("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0004");
        assertNotNull(altoList);
    }

    @Test(expected = AltoNotFoundException.class)
    public void whenAltoNotFoundExceptionShouldBeThrown() {
        File alto = altoRepository.getAltoFile("URN:NBN:no-nb_digibok_2014062307158", "URN:NBN:no-nb_digibok_2014062307158_0040");
    }
}
