package no.nb.microservices.catalogdeliverytext.core.alto.service;

import no.nb.microservices.catalogdeliverytext.model.alto.Alto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface IAltoService {
    InputStream getAltoFilesZipped(String urn, String pages, String pageSelection, String packageFormat) throws IOException;
    InputStream getAltoFilesAsText(String urn, String pages, String pageSelection) throws IOException;
    File getAltoFile(String urn, String pageUrn);

    Alto getAlto(String urn, String pageUrn);
    String getAltoFileAsText(String urn, String pageUrn);
}
