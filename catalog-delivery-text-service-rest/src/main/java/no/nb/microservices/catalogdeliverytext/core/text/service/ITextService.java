package no.nb.microservices.catalogdeliverytext.core.text.service;

import no.nb.microservices.catalogdeliverytext.model.alto.Alto;

public interface ITextService {
    Alto getAltoFile(String id, String pageurn);
}
