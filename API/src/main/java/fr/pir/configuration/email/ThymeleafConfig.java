package fr.pir.configuration.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Configuration
public class ThymeleafConfig {

    private static final Logger L = LogManager.getLogger(ThymeleafConfig.class);

    @Bean
    public SpringTemplateEngine templateEngine() {
        L.debug("");

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(stringTemplateResolver());

        return templateEngine;
    }

    @Bean
    public ITemplateResolver stringTemplateResolver() {
        L.debug("");

        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setOrder(1);

        return resolver;
    }

}
