package com.a206.mychelin.exception;

import com.a206.mychelin.web.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PageIndexLessThanZeroException.class)
    public ResponseEntity<Response> PageIndexLessThanZeroException(PageIndexLessThanZeroException ex) {
        return Response.newResult(HttpStatus.BAD_REQUEST, "page가 0이하이거나 page size가 0이하입니다.", null);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Response> MaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return Response.newResult(HttpStatus.BAD_REQUEST, "이미지 사이즈가 너무 큽니다.", null);
    }
}