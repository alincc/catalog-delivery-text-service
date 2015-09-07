package no.nb.microservices.catalogdeliverytext.core.metadata;

import no.nb.microservices.catalogdeliverytext.core.metadata.repository.CatalogMetadataRepository;
import no.nb.microservices.catalogdeliverytext.core.metadata.service.CatalogMetadataService;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 07.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CatalogMetadataServiceTest {

    @InjectMocks
    CatalogMetadataService catalogMetadataService;

    @Mock
    CatalogMetadataRepository catalogMetadataRepository;

    @Test
    public void getItemPageUrnsTest() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.nb.microservices.catalogmetadata.model");
        String structString = "";
        try {
            structString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/struct/urn:nbn:no-nb_digibok_2014062307158.xml").toURI())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StructMap structMap = (StructMap) marshaller.unmarshal(new StreamSource(new StringReader(structString)));

        assertNotNull(structMap);

        String id = "urn:nbn:no-nb_digibok_2014062307158";
        when(catalogMetadataRepository.getStructure(eq(id))).thenReturn(structMap);

        List<String> urns = catalogMetadataService.getItemPageUrns(id, "1", "id");
        assertEquals(1, urns.size());
        assertEquals("URN:NBN:no-nb_digibok_2014062307158_0002", urns.get(0));

        urns = catalogMetadataService.getItemPageUrns(id, "", "id");
        assertEquals(12, urns.size());

        urns = catalogMetadataService.getItemPageUrns(id, "4,5", "label");
        assertEquals(2, urns.size());
        assertEquals("URN:NBN:no-nb_digibok_2014062307158_0007", urns.get(0));
        assertEquals("URN:NBN:no-nb_digibok_2014062307158_0008", urns.get(1));



    }
}
