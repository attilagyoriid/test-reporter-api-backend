package com.ericsson.eea.rv.testreporter.testreporter.security.emailVerification.event;

import com.ericsson.eea.rv.testreporter.testreporter.domain.PasswordResetToken;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;
@Data
public class OnResetPasswordEvent extends ApplicationEvent {
    private String appUrl;
    private PasswordResetToken passwordResetToken;
    private Locale locale;
    private User user;

    public OnResetPasswordEvent(
            User user, Locale locale, String appUrl, PasswordResetToken passwordResetToken) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.passwordResetToken = passwordResetToken;
    }
}
