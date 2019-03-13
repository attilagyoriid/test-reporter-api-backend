package com.ericsson.eea.rv.testreporter.testreporter.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@Data
public class AppConfiguration {

    @Value("${application.name}")
    private String appName;
    @Value("${testreporter.app.confirmregistration.url}")
    private String confirmregistrationUrl;
    @Value("${testreporter.emailFrom}")
    private String emailFrom;
}
