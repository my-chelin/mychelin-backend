package com.a206.mychelin.exception;

import com.a206.mychelin.web.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PageIndexLessThanZeroException.class)
    public ResponseEntity<Response> PageIndexLessThanZeroException(PageIndexLessThanZeroException ex) {
        return Response.newResult(HttpStatus.BAD_REQUEST, "page가 0이하이거나 page size가 0이하입니다.", null);
    }
}