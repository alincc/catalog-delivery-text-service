package no.nb.microservices.catalogdeliverytext.core.text.repository;

import java.io.File;

public interface AltoRepository {
    File getAlto(String urn, String pageurn);
}
