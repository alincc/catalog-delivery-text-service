package no.nb.microservices.catalogdeliverytext.core.alto.repository;

import java.io.File;

public interface AltoRepository {
    File getAltoFile(String urn, String pageurn);
    String getAltoFileAsString(String urn, String pageurn);
}
