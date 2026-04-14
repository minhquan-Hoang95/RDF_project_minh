package fr.pir.service.rdf;

import java.util.List;

import javax.security.auth.login.AccountException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Annotation;
import fr.pir.model.rdf.Campaign;
import fr.pir.model.user.User;
import fr.pir.repository.rdf.AnnotationRepository;
import fr.pir.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class AnnotationService {

    private static final Logger L = LogManager.getLogger(AnnotationService.class);

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private UserService userService;

    public List<Annotation> getAnnotationsByUser(HttpServletRequest request) throws NotFoundException {
        L.debug("");

        User user = this.userService.getUserFromToken(request);

        List<Annotation> annotations = this.annotationRepository.findAllByCreator(user);

        return annotations;
    }

    @Transactional
    public Annotation createAnnotation(Annotation annotation, HttpServletRequest request) throws Exception {
        L.debug("description : {}", annotation.getDescription());

        if (annotation.getDescription() == null || annotation.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }

        if (annotation.getItem() == null || annotation.getItem().getItemValue() == null
                || annotation.getItem().getItemValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Annotated item cannot be empty.");
        }

        Campaign campaign = this.campaignService.getCampaignById(annotation.getCampaign().getId(), request);
        User user = this.userService.getUserFromToken(request);

        annotation.setCampaign(campaign);
        annotation.setCreator(user);

        this.annotationRepository.save(annotation);

        return annotation;
    }

    @Transactional
    public Annotation updateAnnotation(Long id, String description, HttpServletRequest request) throws Exception {
        L.debug("id : {}, description : {}", id, description);

        User user = this.userService.getUserFromToken(request);

        Annotation annotation = this.annotationRepository.findById(id).orElse(null);

        if (annotation == null) {
            throw new NotFoundException("Annotation not found.");
        }

        if (!annotation.getCreator().getId().equals(user.getId())) {
            throw new AccountException("Unauthorized.");
        }

        annotation.setDescription(description);

        this.annotationRepository.save(annotation);

        return annotation;
    }

}
