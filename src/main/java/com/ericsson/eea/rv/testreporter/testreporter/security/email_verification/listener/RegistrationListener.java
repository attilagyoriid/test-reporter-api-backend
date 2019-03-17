package com.ericsson.eea.rv.testreporter.testreporter.security.email_verification.listener;

import com.ericsson.eea.rv.testreporter.testreporter.configuration.AppConfiguration;
import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.security.email_verification.event.OnRegistrationCompleteEvent;
import com.ericsson.eea.rv.testreporter.testreporter.security.email_verification.event.OnResendRegistrationTokenEvent;
import com.ericsson.eea.rv.testreporter.testreporter.security.email_verification.event.OnResetPasswordEvent;
import com.ericsson.eea.rv.testreporter.testreporter.services.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener {

    private final AppConfiguration appConfiguration;
    private UserService userService;

    private MessageSource messages;

//    private JavaMailSender mailSender;

    public RegistrationListener(UserService userService, MessageSource messages, AppConfiguration appConfiguration) {
        this.userService = userService;
        this.messages = messages;
//        this.mailSender = mailSender;
        this.appConfiguration = appConfiguration;
    }

    @EventListener
    public void onRegistrationCompleteEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    @EventListener
    public void onResendRegistrationTokenEvent(OnResendRegistrationTokenEvent event) {
        this.constructResendVerificationTokenEmail(event);
    }

    @EventListener
    public void onResetPasswordEvent(OnResetPasswordEvent event) {
        this.constructResetPasswordEmail(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);

        String recipientAddress = user.getEmail();
        String subject = appConfiguration.getAppName() + " Registration Confirmation";
        String confirmationUrl
                = appConfiguration.getConfirmregistrationUrl() + "?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " rn " + confirmationUrl);
        System.out.println(email.toString());
//        mailSender.send(email);
    }

    private void constructResendVerificationTokenEmail(OnResendRegistrationTokenEvent event) {
        User user = event.getUser();


        String recipientAddress = user.getEmail();
        String subject = "Email verification re-sent";
        String confirmationUrl
                = appConfiguration.getConfirmregistrationUrl() + "?token=" + event.getVerificationToken().getToken();
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " rn " + confirmationUrl);
        System.out.println(email.toString());
//        mailSender.send(email);
    }

    private void constructResetPasswordEmail(OnResetPasswordEvent event) {

        User user = event.getUser();


        String recipientAddress = user.getEmail();
        String subject = "Email password reset";
        String confirmationUrl
                = event.getAppUrl() + "/api/v1/auth/changePassword?id=" + user.getId() + "&token=" + event.getPasswordResetToken().getToken();
        String message = messages.getMessage("message.resetPassword", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " rn " + confirmationUrl);
        System.out.println(email.toString());
//        mailSender.send(email);

    }


}
