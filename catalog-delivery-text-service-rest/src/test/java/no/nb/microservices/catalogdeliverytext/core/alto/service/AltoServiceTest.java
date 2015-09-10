package no.nb.microservices.catalogdeliverytext.core.alto.service;

import no.nb.microservices.catalogdeliverytext.core.alto.repository.AltoRepository;
import no.nb.microservices.catalogdeliverytext.core.metadata.service.ICatalogMetadataService;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 07.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class AltoServiceTest {

    @Mock
    private AltoRepository altoRepository;

    @Mock
    private ICatalogMetadataService catalogMetadataService;

    private IAltoService altoService;
    private Jaxb2Marshaller marshaller;

    @Before
    public void setup() {
        marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.nb.microservices.catalogdeliverytext.model");

        altoService = new AltoService(catalogMetadataService, altoRepository, marshaller);
    }

    @Test
    public void getAltoFilesZippedTest() throws IOException {
        String id = "urn:nbn:no-nb_digibok_2014062307158";
        List<String> urns = Arrays.asList("digibok_2014062307158_C1", "digibok_2014062307158_I1", "digibok_2014062307158_0001");
        when(catalogMetadataService.getItemPageUrns(eq(id), eq("1-3"), eq("id"))).thenReturn(urns);

        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        when(altoRepository.getAltoFile(eq(id), eq(urns.get(0)))).thenReturn(new File(path + "/alto/" + id + "/" + urns.get(0)));
        when(altoRepository.getAltoFile(eq(id), eq(urns.get(1)))).thenReturn(new File(path + "/alto/" + id + "/" + urns.get(1)));
        when(altoRepository.getAltoFile(eq(id), eq(urns.get(2)))).thenReturn(new File(path + "/alto/" + id + "/" + urns.get(2)));

        File altos = altoService.getAltoFilesZipped(id, "", "id");
        assertNotNull(altos);
        assertEquals("zip", FilenameUtils.getExtension(altos.getPath()));
        assertTrue(altos.length() > 0);
    }

    @Test
    public void getAltoFilesAsTextZippedTest() throws Exception {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        List<String> urns = Arrays.asList("digibok_2014062307158_0002", "digibok_2014062307158_0003", "digibok_2014062307158_0004");
        when(catalogMetadataService.getItemPageUrns(eq(urn), eq("2-4"), eq("id"))).thenReturn(urns);

        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        when(altoRepository.getAltoFileAsString(eq(urn), eq(urns.get(0)))).thenReturn(FileUtils.readFileToString(new File(path + "/alto/" + urn + "/" + urns.get(0) + ".xml")));
        when(altoRepository.getAltoFileAsString(eq(urn), eq(urns.get(1)))).thenReturn(FileUtils.readFileToString(new File(path + "/alto/" + urn + "/" + urns.get(1) + ".xml")));
        when(altoRepository.getAltoFileAsString(eq(urn), eq(urns.get(2)))).thenReturn(FileUtils.readFileToString(new File(path + "/alto/" + urn + "/" + urns.get(2) + ".xml")));

        File zippedFile = altoService.getAltoFilesAsTextZipped(urn, "2-4", "id");
        assertNotNull(zippedFile);
        assertEquals("zip", FilenameUtils.getExtension(zippedFile.getPath()));
        assertTrue(zippedFile.length() > 0);
    }

    @Test
    public void getAltoFileAsTextTest() throws Exception {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        when(altoRepository.getAltoFileAsString(urn, urn + "_0004")).thenReturn(FileUtils.readFileToString(new File(path + "/alto/urn:nbn:no-nb_digibok_2014062307158/digibok_2014062307158_0004.xml")));
        String altoFileAsText = altoService.getAltoFileAsText(urn, urn + "_0004");
        assertNotNull(altoFileAsText);
        assertTrue(altoFileAsText.startsWith("Henrik Ibsen er det moderne dramas far."));
    }

    @Test(expected = AltoNotFoundException.class)
    public void getAltoFileTest() {
        when(altoRepository.getAltoFileAsString("urn:nbn:no-nb_digibok_2014062307158","urn:nbn:no-nb_digibok_2014062307158_0004")).thenReturn(null);
        altoService.getAltoFile("urn:nbn:no-nb_digibok_2014062307158","urn:nbn:no-nb_digibok_2014062307158_0004");
    }
}
