package no.nb.microservices.catalogdeliverytext.core.text.repository;

import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.net.URL;

@Repository
public class DiskAltoRepository implements AltoRepository {

    @Override
    public File getAlto(String urn, String pageUrn) {
        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        String altoPath = path + "/alto/" + urn.toLowerCase() + "/" + pageUrn.toLowerCase().replace("urn:nbn:no-nb_","") + ".xml";
        File alto = new File(altoPath);
        if (!alto.exists()) {
            throw new AltoNotFoundException("Alto not found");
        }
        return alto;
    }

}
