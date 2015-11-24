package no.nb.microservices.catalogdeliverytext.core.metadata.repository;

import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("catalog-metadata-service")
public interface CatalogMetadataRepository {
    @RequestMapping(value = "/v1/catalog/metadata/{id}/struct", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    StructMap getStructure(@PathVariable("id") String id);
}
