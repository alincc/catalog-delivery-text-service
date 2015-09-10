package no.nb.microservices.catalogdeliverytext.rest.controller;

import no.nb.microservices.catalogdeliverytext.core.alto.service.IAltoService;
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

    private final IAltoService altoService;

    @Autowired
    public AltoController(IAltoService altoService) {
        this.altoService = altoService;
    }

    @RequestMapping(value = "/alto/{urn}", method = RequestMethod.GET)
    public void getAltos(@PathVariable String urn,
                         @RequestParam(defaultValue = "", required = false) String pages,
                         @RequestParam(defaultValue = "id", required = false) String pageSelection,
                         HttpServletResponse response) throws IOException {
        File altos = altoService.getAltoFilesZipped(urn, pages, pageSelection);
        response.setContentType("application/zip, application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + urn + ".zip");
        response.getOutputStream().write(Files.readAllBytes(altos.toPath()));
    }

    @RequestMapping(value = "/alto/{urn}/{pageUrn}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Alto> getAlto(@PathVariable String urn, @PathVariable String pageUrn) {
        Alto alto = altoService.getAlto(urn, pageUrn);
        return new ResponseEntity(alto, HttpStatus.OK);
    }

    @RequestMapping(value = "/text/{urn}", method = RequestMethod.GET)
    public void getAltosAsText(@PathVariable String urn,
                               @RequestParam(defaultValue = "", required = false) String pages,
                               @RequestParam(defaultValue = "", required = false) String pageSelection,
                               HttpServletResponse response) throws IOException {
        File altos = altoService.getAltoFilesAsTextZipped(urn, pages, pageSelection);
        response.setContentType("application/zip, application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + urn + ".zip");
        response.getOutputStream().write(Files.readAllBytes(altos.toPath()));
    }

    @RequestMapping(value = "/text/{urn}/{pageUrn}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getText(@PathVariable String urn, @PathVariable String pageUrn) {
        String altoFileAsText = altoService.getAltoFileAsText(urn, pageUrn);
        return new ResponseEntity(altoFileAsText, HttpStatus.OK);
    }
}
