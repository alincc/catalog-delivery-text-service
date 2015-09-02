package no.nb.microservices.catalogdeliverytext.core.text.repository;

import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Repository;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Repository
public class DiskAltoRepository implements AltoRepository {
    private final Jaxb2Marshaller marshaller;

    @Autowired
    public DiskAltoRepository(Jaxb2Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @Override
    public Alto getAlto(String urn, String pageurn) {
        String altoString;
        try {
            String path = "/alto/" + urn.toLowerCase() + "/" + pageurn.toLowerCase().replace("urn:nbn:no-nb_","") + ".xml";
            altoString = new String(Files.readAllBytes(Paths.get(getClass().getResource(path).toURI())));
        } catch (Exception e) {
            throw new AltoNotFoundException("Alto not found");
        }

        Alto alto = (Alto) marshaller.unmarshal(new StreamSource(new StringReader(altoString)));

        return alto;
    }

}
