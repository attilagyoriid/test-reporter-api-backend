package com.ericsson.eea.rv.testreporter.testreporter.controllers.handler.error;

import com.ericsson.eea.rv.testreporter.testreporter.error.DetailedResponseMessage;
import com.ericsson.eea.rv.testreporter.testreporter.exceptions.AlreadyExitException;
import com.ericsson.eea.rv.testreporter.testreporter.exceptions.CustomValidationException;
import com.ericsson.eea.rv.testreporter.testreporter.exceptions.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    public static final String EXCLAMATION = "!";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public DetailedResponseMessage handleNotFoundException(NotFoundException ex) {

        return new DetailedResponseMessage(new Date(), "Not Found",
                Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(AlreadyExitException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public DetailedResponseMessage handleAlreadyExitException(AlreadyExitException ex) {

        return new DetailedResponseMessage(new Date(), "Resource Already Exists",
                Collections.singletonList(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public DetailedResponseMessage handleConstraintViolationException(ConstraintViolationException ex) {

        List<String> constraintViolationMessages = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).map(s -> s.split(",")).flatMap(Arrays::stream).collect(Collectors.toList());
        return new DetailedResponseMessage(new Date(), "Incorrect Data",
                constraintViolationMessages);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public DetailedResponseMessage handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        return new DetailedResponseMessage(new Date(), "Validation Failed",
                ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).map(s -> s.split(",",-1)).flatMap(Arrays::stream).collect(Collectors.toList()));
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public DetailedResponseMessage handleCustomValidationException(CustomValidationException ex) {

        return new DetailedResponseMessage(new Date(), "Validation Failed",
                Collections.singletonList(ex.getLocalizedMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public DetailedResponseMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new DetailedResponseMessage(new Date(), "Malformed Json",
                Arrays.asList("Json parse error"));
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public DetailedResponseMessage handleAccessDeniedException(AccessDeniedException ex) {
        return new DetailedResponseMessage(new Date(), HttpStatus.FORBIDDEN.toString(),
                Arrays.asList(ex.getLocalizedMessage() + "!"));
    }

    @ExceptionHandler({DisabledException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public DetailedResponseMessage handleDisabledException(DisabledException ex) {
        return new DetailedResponseMessage(new Date(), HttpStatus.FORBIDDEN.toString(),
                Arrays.asList(ex.getLocalizedMessage() + EXCLAMATION));
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public DetailedResponseMessage handleBadCredentialsException(BadCredentialsException ex) {
        return new DetailedResponseMessage(new Date(), HttpStatus.FORBIDDEN.toString(),
                Arrays.asList(ex.getLocalizedMessage() + "!"));
    }
}


