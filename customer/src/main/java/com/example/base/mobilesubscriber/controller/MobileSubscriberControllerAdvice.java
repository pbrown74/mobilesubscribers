package com.example.base.mobilesubscriber.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.base.AppError;
import com.example.base.mobilesubscriber.exception.BadFormatMobileException;
import com.example.base.mobilesubscriber.exception.BadServiceTypeException;
import com.example.base.mobilesubscriber.exception.DuplicateMobileNumberException;
import com.example.base.mobilesubscriber.exception.UnknownMobileNumberException;
import com.example.base.mobilesubscriber.exception.UnknownSubscriberException;

@RestControllerAdvice
public class MobileSubscriberControllerAdvice {

    @ExceptionHandler(DuplicateMobileNumberException.class)
    public ResponseEntity<AppError> handleDuplicateMobileNumberException(DuplicateMobileNumberException ex) {
        final AppError error = new AppError(
                "1.0",
                Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "DuplicateMobileNumberException",
                "mobilesubscriber-exceptions",
                "You cannot add a mobile subscription for an existing mobile number.",
                "DuplicateMobileNumberException",
                ""
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownMobileNumberException.class)
    public ResponseEntity<AppError> handleUnknownMobileNumberException(UnknownMobileNumberException ex) {
        final AppError error = new AppError(
                "1.0",
                Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "UnknownMobileNumberException",
                "mobilesubscriber-exceptions",
                "Mobile number unknown in database.",
                "UnknownMobileNumberException",
                ""
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownSubscriberException.class)
    public ResponseEntity<AppError> handleUnknownSubscriberException(UnknownSubscriberException ex) {
        final AppError error = new AppError(
                "1.0",
                Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "UnknownSubscriberException",
                "mobilesubscriber-exceptions",
                "Mobile Subscriber Id unknown in database.",
                "UnknownSubscriberException",
                ""
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadFormatMobileException.class)
    public ResponseEntity<AppError> handleBadFormatMobileException(BadFormatMobileException ex) {
        final AppError error = new AppError(
                "1.0",
                Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "BadFormatMobileException",
                "mobilesubscriber-exceptions",
                "Mobile number not in E164 format.",
                "BadFormatMobileException",
                ""
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadServiceTypeException.class)
    public ResponseEntity<AppError> handleBadServiceTypeException(BadServiceTypeException ex) {
        final AppError error = new AppError(
                "1.0",
                Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "BadServiceTypeException",
                "mobilesubscriber-exceptions",
                "Service Type can only be MOBILE_PREPAID or MOBILE_POSTPAID",
                "BadServiceTypeException",
                ""
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
