package fr.pir.controller.rdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.exception.AccountException;
import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Annotation;
import fr.pir.service.rdf.AnnotationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/annotation")
public class AnnotationController {

    private static final Logger L = LogManager.getLogger(AnnotationController.class);

    @Autowired
    private AnnotationService annotationService;

    @PostMapping
    public ResponseEntity<?> createAnnotation(@RequestBody Annotation annotation, HttpServletRequest request) {
        L.debug("description : {}", annotation.getDescription());

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.annotationService.createAnnotation(annotation, request));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccountException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            L.error("Error creating annotation", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateAnnotation(@RequestParam Long id, @RequestParam String name,
            HttpServletRequest request) {
        L.debug("id : {}, name : {}", id, name);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.annotationService.updateAnnotation(id, name, request));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccountException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            L.error("Error updating annotation", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAnnotationsByUser(HttpServletRequest request) {
        L.debug("");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.annotationService.getAnnotationsByUser(request));
        } catch (Exception e) {
            // There's no point in throwing a NotFoundException catch, as we won't be able
            // to retrieve the user from his token.
            L.error("Error getting annotations", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

}
