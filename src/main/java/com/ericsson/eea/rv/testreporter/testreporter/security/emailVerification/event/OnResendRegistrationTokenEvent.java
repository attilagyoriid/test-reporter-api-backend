package com.ericsson.eea.rv.testreporter.testreporter.security.emailVerification.event;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.domain.VerificationToken;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Data
public class OnResendRegistrationTokenEvent extends ApplicationEvent {
    private String appUrl;
    private VerificationToken verificationToken;
    private Locale locale;
    private User user;

    public OnResendRegistrationTokenEvent(
            User user, Locale locale, String appUrl, VerificationToken verificationToken) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.verificationToken = verificationToken;
    }

}