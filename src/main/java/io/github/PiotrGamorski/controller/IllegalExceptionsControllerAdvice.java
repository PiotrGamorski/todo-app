package io.github.PiotrGamorski.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// COMMENT: THE FOLLOWING ANNOTATION ENABLES TO KEEP TRACK ON ERROR HANDLING FROM CONTROLLERS
@RestControllerAdvice(annotations = IllegalExceptionProcessing.class)
public class IllegalExceptionsControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalStateException(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
