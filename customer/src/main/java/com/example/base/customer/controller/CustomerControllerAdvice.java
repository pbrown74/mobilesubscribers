package com.example.base.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.base.AppError;
import com.example.base.customer.exception.BadIdCardFormatException;
import com.example.base.customer.exception.DuplicateCustomerIdCardException;
import com.example.base.customer.exception.UnknownCustomerException;

@RestControllerAdvice
public class CustomerControllerAdvice {

    @ExceptionHandler(DuplicateCustomerIdCardException.class)
    public ResponseEntity<AppError> handleDuplicateCustomerIdCardException(DuplicateCustomerIdCardException ex) {
        final AppError error = new AppError("1.0", Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "DuplicateCustomerIdCardException", "customer-exceptions",
                "You cannot add a customer for an existing ID Card.", "DuplicateCustomerIdCardException", "");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownCustomerException.class)
    public ResponseEntity<AppError> handleUnknownCustomerException(UnknownCustomerException ex) {
        final AppError error = new AppError("1.0", Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "UnknownCustomerException", "customer-exceptions", "Customer requested with unknown id",
                "UnknownCustomerException", "");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadIdCardFormatException.class)
    public ResponseEntity<AppError> handleBadIdCardFormatException(BadIdCardFormatException ex) {
        final AppError error = new AppError("1.0", Integer.toString(HttpStatus.BAD_REQUEST.value()),
                "BadIdCardFormatException", "customer-exceptions", "ID Card format must be XXXXXXX[M,G,A,P,L,H,B,Z]",
                "BadIdCardFormatException", "");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
