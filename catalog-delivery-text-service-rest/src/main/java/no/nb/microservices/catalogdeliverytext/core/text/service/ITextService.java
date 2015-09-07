package no.nb.microservices.catalogdeliverytext.core.text.service;

import no.nb.microservices.catalogdeliverytext.model.alto.Alto;

import java.io.File;

public interface ITextService {
    Alto getAlto(String id, String pageUrn);

    File getAltoFile(String id, String pageUrn);
}
