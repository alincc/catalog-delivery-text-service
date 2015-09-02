package no.nb.microservices.catalogdeliverytext.core.text.repository;

import no.nb.microservices.catalogdeliverytext.model.alto.Alto;

public interface AltoRepository {
    Alto getAlto(String urn, String pageurn);
}
