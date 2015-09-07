package no.nb.microservices.catalogdeliverytext.core.metadata.service;

import java.util.List;

/**
 * Created by andreasb on 03.09.15.
 */
public interface ICatalogMetadataService {
    List<String> getItemPageUrns(String id, String pages, String pageSelection);
}
