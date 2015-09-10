package no.nb.microservices.catalogdeliverytext.core.alto.service;

import no.nb.microservices.catalogdeliverytext.model.alto.Alto;

import java.io.File;
import java.io.IOException;

public interface IAltoService {
    File getAltoFilesZipped(String urn, String pages, String pageSelection) throws IOException;
    File getAltoFilesAsTextZipped(String urn, String pages, String pageselection) throws IOException;
    File getAltoFile(String urn, String pageUrn);

    Alto getAlto(String urn, String pageUrn);
    String getAltoFileAsText(String urn, String pageUrn);
}
