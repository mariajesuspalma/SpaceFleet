package com.primeit.spacefleet.controller;

import com.primeit.spacefleet.exception.NegativeIdException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(NegativeIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleNegativeId(NegativeIdException exception) {
        ErrorResponse errorResponse = new ErrorResponse("400", "The identifier is not correct");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFound(ChangeSetPersister.NotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse("404", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInternalError(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("500", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public static class ErrorResponse {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ErrorResponse() {
        }

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}

