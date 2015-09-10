package no.nb.microservices.catalogdeliverytext.core.alto.repository;

import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Repository
public class DiskAltoRepository implements AltoRepository {

    @Override
    public File getAltoFile(String urn, String pageUrn) {
        URL resource = getClass().getResource("/");
        String path = resource.getPath();
        String altoPath = path + "/alto/" + urn.toLowerCase() + "/" + pageUrn.toLowerCase().replace("urn:nbn:no-nb_","") + ".xml";
        File alto = new File(altoPath);
        if (!alto.exists()) {
            throw new AltoNotFoundException("Alto not found");
        }
        return alto;
    }

    @Override
    public String getAltoFileAsString(String urn, String pageurn) {
        File altoFile = getAltoFile(urn, pageurn);
        String altoString = null;
        try {
            altoString = FileUtils.readFileToString(altoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return altoString;
    }
}
