package no.nb.microservices.catalogdeliverytext.core.alto;

import no.nb.microservices.catalogdeliverytext.core.metadata.service.ICatalogMetadataService;
import no.nb.microservices.catalogdeliverytext.core.text.service.ITextService;
import no.nb.microservices.catalogdeliverytext.utility.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 04.09.15.
 */
@Service
public class AltoService implements IAltoService {

    private final ITextService textService;
    private final ICatalogMetadataService catalogMetadataService;

    @Autowired
    public AltoService(ITextService textService, ICatalogMetadataService catalogMetadataService) {
        this.textService = textService;
        this.catalogMetadataService = catalogMetadataService;
    }

    @Override
    public File getAltos(String id, String pages, String pageSelection) throws IOException {
        List<String> urns = catalogMetadataService.getItemPageUrns(id, pages, pageSelection);
        List<File> altoFiles = urns.stream().map(q -> textService.getAltoFile(id, q)).collect(Collectors.toList());
        File zippedFile = ZipUtils.zip(altoFiles, "/tmp");
        return zippedFile;
    }
}
