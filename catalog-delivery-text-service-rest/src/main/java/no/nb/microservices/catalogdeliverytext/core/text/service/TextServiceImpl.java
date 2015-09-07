package no.nb.microservices.catalogdeliverytext.core.text.service;

import no.nb.microservices.catalogdeliverytext.core.text.repository.AltoRepository;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;


@Service
public class TextServiceImpl implements ITextService {
    private final AltoRepository altoRepository;
    private final Jaxb2Marshaller marshaller;

    @Autowired
    public TextServiceImpl(AltoRepository altoRepository, Jaxb2Marshaller marshaller) {
        this.altoRepository = altoRepository;
        this.marshaller = marshaller;
    }

    @Override
    public Alto getAlto(String urn, String pageUrn) {
        File altoFile = altoRepository.getAlto(urn, pageUrn);
        if (altoFile == null) {
            throw new AltoNotFoundException("");
        }

        String altoString;
        try {
            //altoString = new String(Files.readAllBytes(Paths.get(getClass().getResource(altoFile.getPath()).toURI())));
            altoString = FileUtils.readFileToString(altoFile);
        } catch (Exception e) {
            throw new AltoNotFoundException("Alto not found");
        }

        Alto alto = (Alto) marshaller.unmarshal(new StreamSource(new StringReader(altoString)));

        return alto;
    }

    @Override
    public File getAltoFile(String id, String pageUrn) {
        File file = altoRepository.getAlto(id, pageUrn);
        if (file == null) {
            throw new AltoNotFoundException("");
        }
        return file;
    }
}
