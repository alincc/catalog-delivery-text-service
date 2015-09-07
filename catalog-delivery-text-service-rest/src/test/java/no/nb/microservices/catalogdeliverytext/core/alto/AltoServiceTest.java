package no.nb.microservices.catalogdeliverytext.core.alto;

import no.nb.microservices.catalogdeliverytext.core.metadata.service.ICatalogMetadataService;
import no.nb.microservices.catalogdeliverytext.core.text.service.ITextService;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 07.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class AltoServiceTest {

    @InjectMocks
    private AltoService altoService;

    @Mock
    private ITextService textService;

    @Mock
    private ICatalogMetadataService catalogMetadataService;

    @Test
    public void getAltosTest() throws IOException {
        String id = "urn:nbn:no-nb_digibok_2014062307158";
        List<String> urns = Arrays.asList("digibok_2014062307158_C1", "digibok_2014062307158_I1", "digibok_2014062307158_0001");
        when(catalogMetadataService.getItemPageUrns(eq(id), eq("1-3"), eq("id"))).thenReturn(urns);

        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        when(textService.getAltoFile(eq(id), eq(urns.get(0)))).thenReturn(new File(path + "/alto/" + id + "/" + urns.get(0)));
        when(textService.getAltoFile(eq(id), eq(urns.get(1)))).thenReturn(new File(path + "/alto/" + id + "/" + urns.get(1)));
        when(textService.getAltoFile(eq(id), eq(urns.get(2)))).thenReturn(new File(path + "/alto/" + id + "/" + urns.get(2)));

        File altos = altoService.getAltos(id, "", "id");
        assertNotNull(altos);
        assertEquals("zip", FilenameUtils.getExtension(altos.getPath()));
        assertTrue(altos.length() > 0);
    }
}
