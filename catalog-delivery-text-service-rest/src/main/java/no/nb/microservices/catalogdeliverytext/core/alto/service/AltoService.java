package no.nb.microservices.catalogdeliverytext.core.alto.service;

import no.nb.commons.io.packaging.factory.PackageFactory;
import no.nb.commons.io.packaging.service.PackageService;
import no.nb.microservices.catalogdeliverytext.core.alto.repository.AltoRepository;
import no.nb.microservices.catalogdeliverytext.core.metadata.service.ICatalogMetadataService;
import no.nb.microservices.catalogdeliverytext.exception.AltoNotFoundException;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import no.nb.microservices.catalogdeliverytext.utility.AltoTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AltoService implements IAltoService {

    private final ICatalogMetadataService catalogMetadataService;
    private final AltoRepository altoRepository;
    private final Jaxb2Marshaller marshaller;
    private final PackageFactory packageFactory;

    @Autowired
    public AltoService(ICatalogMetadataService catalogMetadataService, AltoRepository altoRepository, Jaxb2Marshaller marshaller, PackageFactory packageFactory) {
        this.catalogMetadataService = catalogMetadataService;
        this.altoRepository = altoRepository;
        this.marshaller = marshaller;
        this.packageFactory = packageFactory;
    }

    @Override
    public InputStream getAltoFilesZipped(String urn, String pages, String pageSelection, String packageFormat) throws IOException {
        PackageService packageService = packageFactory.getPackageService(packageFormat);
        List<String> urns = catalogMetadataService.getItemPageUrns(urn, pages, pageSelection);
        List<File> altoFiles = urns.stream().map(q -> getAltoFile(urn, q)).collect(Collectors.toList());
        return packageService.packIt(altoFiles);
    }

    @Override
    public InputStream getAltoFilesAsText(String urn, String pages, String pageSelection) throws IOException {
        List<String> urns = catalogMetadataService.getItemPageUrns(urn, pages, pageSelection);
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : urns) {
            stringBuilder.append(getAltoFileAsText(urn, s));
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append("-----");
            stringBuilder.append(System.lineSeparator());
        }

        return new ByteArrayInputStream(stringBuilder.toString().getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public File getAltoFile(String urn, String pageUrn) {
        File file = altoRepository.getAltoFile(urn, pageUrn);
        if (file == null) {
            throw new AltoNotFoundException("");
        }
        return file;
    }

    @Override
    public Alto getAlto(String urn, String pageUrn) {
        String altoString = altoRepository.getAltoFileAsString(urn, pageUrn);
        if (altoString == null) {
            throw new AltoNotFoundException("");
        }
        return (Alto) marshaller.unmarshal(new StreamSource(new StringReader(altoString)));
    }

    @Override
    public String getAltoFileAsText(String urn, String pageUrn) {
        Alto alto = this.getAlto(urn, pageUrn);
        return AltoTextExtractor.extractText(alto);
    }
}
