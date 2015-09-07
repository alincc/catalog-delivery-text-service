package no.nb.microservices.catalogdeliverytext.core.alto;

import java.io.File;
import java.io.IOException;

/**
 * Created by andreasb on 04.09.15.
 */
public interface IAltoService {
    File getAltos(String id, String pages, String pageSelection) throws IOException;
}
