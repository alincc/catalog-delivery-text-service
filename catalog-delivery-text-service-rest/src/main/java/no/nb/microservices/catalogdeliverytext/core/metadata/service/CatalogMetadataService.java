package no.nb.microservices.catalogdeliverytext.core.metadata.service;

import no.nb.microservices.catalogdeliverytext.core.metadata.repository.CatalogMetadataRepository;
import no.nb.microservices.catalogdeliverytext.utility.PageUtils;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 03.09.15.
 */
@Service
public class CatalogMetadataService implements ICatalogMetadataService {

    private final CatalogMetadataRepository catalogMetadataRepository;

    @Autowired
    public CatalogMetadataService(CatalogMetadataRepository catalogMetadataRepository) {
        this.catalogMetadataRepository = catalogMetadataRepository;
    }

    @Override
    public List<String> getItemPageUrns(String id, String pages, String pageSelection) {
        StructMap structMap =  catalogMetadataRepository.getStructure(id);
        List<Integer> pagesList = PageUtils.toPageList(pages);
        List<String> itemUrns = structMap.getDivs().stream()
                .filter(q -> (pagesList.isEmpty()) || ("id".equals(pageSelection) ? pagesList.contains(structMap.getDivs().indexOf(q)) : pagesList.contains(Integer.parseInt((q.getOrderLabel() != null ? q.getOrderLabel() : "-1")))))
                .map(q -> q.getResource().getHref())
                .collect(Collectors.toList());

        return itemUrns;

    }


}
