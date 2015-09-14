package no.nb.microservices.catalogdeliverytext.core.searchindex.service;

import no.nb.microservices.catalogdeliverytext.core.searchindex.repository.SearchIndexRepository;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SearchResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchIndexService implements ISearchIndexService {
    private final SearchIndexRepository searchIndexRepository;

    @Autowired
    public SearchIndexService(SearchIndexRepository searchIndexRepository) {
        this.searchIndexRepository = searchIndexRepository;
    }

    @Override
    public String getId(String urn) {
        SearchResource searchResource = searchIndexRepository.search("urn:\"" + urn + "\"", null, 0,1,null);
        List<ItemResource> items = searchResource.getEmbedded().getItems();
        return items.get(0).getItemId();
    }
}
