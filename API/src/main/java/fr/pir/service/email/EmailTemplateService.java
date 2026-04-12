package fr.pir.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.pir.model.email.EmailTemplate;
import fr.pir.repository.email.EmailTemplateRepository;

@Service
public class EmailTemplateService {

    private static final Logger L = LogManager.getLogger(EmailTemplateService.class);

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public EmailTemplate getEmailTemplateByName(String name) {
        L.debug("name : {}", name);

        return this.emailTemplateRepository.findEmailTemplateByName(name);
    }

}
