/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.controller;

import com.usetech.bridge.bean.ErrorBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status((HttpStatus) HttpStatus.BAD_REQUEST).body((Object) new ErrorBean(ex.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> badRequest(HttpServletRequest req, Exception exception) {
        return ResponseEntity.status((HttpStatus) HttpStatus.INTERNAL_SERVER_ERROR)
                .body((Object) new ErrorBean(exception.getMessage()));
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status((HttpStatus) HttpStatus.BAD_REQUEST).body((Object) new ErrorBean(ex.getMessage()));
    }
}