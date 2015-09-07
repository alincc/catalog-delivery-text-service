package no.nb.microservices.catalogdeliverytext.rest.controller;

import no.nb.microservices.catalogdeliverytext.core.alto.IAltoService;
import no.nb.microservices.catalogdeliverytext.core.text.service.ITextService;
import no.nb.microservices.catalogdeliverytext.model.alto.Alto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class AltoController {
    private static final Logger LOG = LoggerFactory.getLogger(AltoController.class);

    private final ITextService textService;
    private final IAltoService altoService;

    @Autowired
    public AltoController(ITextService textService, IAltoService altoService) {
        this.textService = textService;
        this.altoService = altoService;
    }

    @RequestMapping(value = "/alto/{urn}", method = RequestMethod.GET)
    public void getAltos(@PathVariable String urn,
                         @RequestParam(defaultValue = "", required = false) String pages,
                         @RequestParam(defaultValue = "id", required = false) String pageSelection,
                         HttpServletResponse response) throws IOException {
        File altos = altoService.getAltos(urn, pages, pageSelection);
        response.setContentType("application/zip, application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + urn + ".zip");
        response.getOutputStream().write(Files.readAllBytes(altos.toPath()));
    }

    @RequestMapping(value = "/alto/{urn}/{pageUrn}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Alto> getAlto(@PathVariable String urn, @PathVariable String pageUrn) {
        Alto alto = textService.getAlto(urn, pageUrn);
        return new ResponseEntity(alto, HttpStatus.OK);
    }


}
