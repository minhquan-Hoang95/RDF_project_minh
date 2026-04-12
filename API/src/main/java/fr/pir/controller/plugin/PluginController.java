package fr.pir.controller.plugin;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.pir.service.plugin.PluginService;

@Controller
@RequestMapping("/api/plugin")
public class PluginController {

    private static final Logger L = LogManager.getLogger(PluginController.class);

    @Autowired
    private PluginService pluginService;

    @GetMapping
    public ResponseEntity<?> downloadPlugin() {
        L.debug("");

        try {
            File zippedFile = this.pluginService.zipFolder();

            Resource resource = new FileSystemResource(zippedFile);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"plugin.zip\"")
                    .contentLength(zippedFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            L.error("Error creating or downloading plugin", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

}
