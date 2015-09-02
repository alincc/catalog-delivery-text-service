package no.nb.microservices.catalogdeliverytext.core.text.service;

import no.nb.microservices.catalogdeliverytext.core.text.repository.AltoRepository;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TextServiceImpl implements ITextService {
    private final AltoRepository altoRepository;

    @Autowired
    public TextServiceImpl(AltoRepository altoRepository) {
        this.altoRepository = altoRepository;
    }

    @Override
    public Alto getAltoFile(String urn, String pageurn) {
        Alto alto = altoRepository.getAlto(urn, pageurn);
        if (alto == null) {
            throw new AltoNotFoundException("");
        }
        return alto;
    }

}
