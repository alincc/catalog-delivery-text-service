package no.nb.microservices.catalogdeliverytext.rest.controller;

import no.nb.microservices.catalogdeliverytext.core.text.service.ITextService;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private final ITextService textService;

    @Autowired
    public HomeController(ITextService textService) {
        this.textService = textService;
    }

    @RequestMapping(value = "/alto/{urn}/{pageurn}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Alto> getAlto(@PathVariable String urn, @PathVariable String pageurn) {
        Alto alto = textService.getAltoFile(urn, pageurn);
        return new ResponseEntity(alto, HttpStatus.OK);
    }
}
