package fr.pir.service.email;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.model.email.EmailTemplate;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger L = LogManager.getLogger(EmailService.class);

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    public void sendEmailWithTemplate(String to, String templateName, Map<String, Object> templateInfos)
            throws MessagingException, IOException {
        L.debug("to : {}, templateName : {}", to, templateName);

        EmailTemplate emailTemplate = this.emailTemplateService.getEmailTemplateByName(templateName);

        templateInfos.put("apiBaseUrl", this.apiBaseUrl);

        Context context = new Context();
        context.setVariables(templateInfos);

        String htmlContent = this.templateEngine.process(emailTemplate.getContent(), context);

        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(this.sender);
        helper.setTo(to);
        helper.setSubject(emailTemplate.getSubject());
        helper.setText(htmlContent, true);

        this.mailSender.send(message);
    }

}
