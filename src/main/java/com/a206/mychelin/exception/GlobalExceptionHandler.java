package com.a206.mychelin.exception;

import com.a206.mychelin.web.dto.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PageIndexLessThanZeroException.class)
    public ResponseEntity PageIndexLessThanZeroException(PageIndexLessThanZeroException ex) {
        CustomResponseEntity result = CustomResponseEntity.builder()
                .status(400)
                .message("page가 0이하이거나 pagesize가 0이하입니다.")
                .build();

        return new ResponseEntity<CustomResponseEntity>(result, HttpStatus.BAD_REQUEST);
    }
}